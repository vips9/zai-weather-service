package com.example.weather.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherResponse {
  private double temperatureDegrees;
  private double windSpeed;
}
