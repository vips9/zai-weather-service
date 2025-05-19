

package com.example.weather.config;

import com.example.weather.model.WeatherResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    @Bean("weatherCache")
    public Cache<String, WeatherResponse> weatherCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .maximumSize(20)
                .build();
    }
}
