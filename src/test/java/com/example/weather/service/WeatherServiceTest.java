package com.example.weather.service;

import com.example.weather.exception.ExternalServiceException;
import com.example.weather.model.WeatherResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    Cache<String, WeatherResponse> realCache;
    @Mock
    private WeatherClient primary;
    @Mock
    private WeatherClient fallback;
    @Mock
    private Cache<String, WeatherResponse> cache;
    @InjectMocks
    private WeatherService svc;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        realCache = Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
        svc = new WeatherService(primary, fallback, realCache);
    }

    @Test
    void whenPrimarySucceeds_returnPrimary() {
        WeatherService svc = new WeatherService(primary, fallback, realCache);
        when(primary.getWeather("Melbourne"))
                .thenReturn(new WeatherResponse(20, 10));
        WeatherResponse result = svc.getWeather("Melbourne");
        assertEquals(20, result.getTemperatureDegrees());
        verify(primary).getWeather("Melbourne");
        verifyNoInteractions(fallback);
    }


    @Test
    void whenPrimaryFails_useFallback() {
        WeatherService svc = new WeatherService(primary, fallback, realCache);
        when(primary.getWeather("Melbourne"))
                .thenThrow(new ExternalServiceException("primary down", null));
        when(fallback.getWeather("Melbourne"))
                .thenReturn(new WeatherResponse(15, 5));
        WeatherResponse result = svc.getWeather("Melbourne");
        assertEquals(15, result.getTemperatureDegrees());
        verify(primary).getWeather("Melbourne");
        verify(fallback).getWeather("Melbourne");
    }
}
