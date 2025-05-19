package com.example.weather.service;

import com.example.weather.exception.ExternalServiceException;
import com.example.weather.model.WeatherResponse;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class WeatherService {
  private final WeatherClient primary, fallback;
  private final ConcurrentMap<String,WeatherResponse> staleMap = new ConcurrentHashMap<>();

  public WeatherService(
          @Qualifier("weatherStackClient") WeatherClient primary,
          @Qualifier("openWeatherMapClient") WeatherClient fallback
  ) {
    this.primary = primary;
    this.fallback = fallback;
  }

  @Cacheable(value="weather", key="#city")
  public WeatherResponse getWeather(String city) {
    try {
      WeatherResponse r = primary.getWeather(city);
      staleMap.put(city, r);
      return r;
    } catch (Exception primaryEx) {
      try {
        WeatherResponse r = fallback.getWeather(city);
        staleMap.put(city, r);
        return r;
      } catch (Exception fallbackEx) {
        WeatherResponse stale = staleMap.get(city);
        if (stale != null) {
          return stale;
        }
        throw new ExternalServiceException(
                "All weather providers failed and no cached data", fallbackEx
        );
      }
    }
  }
}
