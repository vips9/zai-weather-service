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
  private final WeatherClient primary;
  private final WeatherClient fallback;

  private final Cache<String, WeatherResponse> cache;
  private final ConcurrentMap<String, WeatherResponse> staleMap = new ConcurrentHashMap<>();


  public WeatherService(
          @Qualifier("weatherStackClient") WeatherClient primary,
          @Qualifier("openWeatherMapClient") WeatherClient fallback,
          @Qualifier("weatherCache") Cache<String, WeatherResponse> cache
  ) {
    this.primary = primary;
    this.fallback = fallback;
    this.cache   = cache;
  }


  public WeatherResponse getWeather(String city) {
    WeatherResponse resp = cache.getIfPresent(city);
    if (resp == null) {
      try {
        resp = primary.getWeather(city);
      } catch (Exception primaryEx) {
        try {
          resp = fallback.getWeather(city);
        } catch (Exception fallbackEx) {
          WeatherResponse stale = staleMap.get(city);
          if (stale != null) {
            return stale;
          }
          throw new ExternalServiceException(
                  "All weather providers failed and no cached data",
                  fallbackEx
          );
        }
      }
      cache.put(city, resp);
      staleMap.put(city, resp);
    }
    return resp;
  }
}
