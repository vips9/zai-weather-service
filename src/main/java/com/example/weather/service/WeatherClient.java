package com.example.weather.service;

import com.example.weather.model.WeatherResponse;

public interface WeatherClient {
  WeatherResponse getWeather(String city);
}
