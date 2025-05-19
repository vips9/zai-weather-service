package com.example.weather.service;

import com.example.weather.exception.ExternalServiceException;
import com.example.weather.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class WeatherServiceTest {

    @Mock
    private WeatherClient primary;

    @Mock
    private WeatherClient fallback;

    private WeatherService svc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        svc = new WeatherService(primary, fallback);
    }

    @Test
    void whenPrimarySucceeds_returnPrimary() {
        when(primary.getWeather("Melbourne"))
                .thenReturn(new WeatherResponse(20, 10));
        WeatherResponse result = svc.getWeather("Melbourne");
        Assertions.assertEquals(20, result.getTemperatureDegrees());
        verify(primary).getWeather("Melbourne");
        verifyNoInteractions(fallback);
    }

    @Test
    void whenPrimaryFails_useFallback() {
        when(primary.getWeather("Melbourne"))
                .thenThrow(new ExternalServiceException("primary down", null));
        when(fallback.getWeather("Melbourne"))
                .thenReturn(new WeatherResponse(15, 5));
        WeatherResponse result = svc.getWeather("Melbourne");
        Assertions.assertEquals(15, result.getTemperatureDegrees());
        verify(primary).getWeather("Melbourne");
        verify(fallback).getWeather("Melbourne");
    }
}
