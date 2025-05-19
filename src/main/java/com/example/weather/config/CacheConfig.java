

package com.example.weather.config;

import com.example.weather.model.WeatherResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager mgr = new CaffeineCacheManager("weather");
        mgr.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .maximumSize(20));
        return mgr;
    }
}
