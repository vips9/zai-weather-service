package com.example.weather.service.impl;

import com.example.weather.exception.ExternalServiceException;
import com.example.weather.model.WeatherResponse;
import com.example.weather.service.WeatherClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component("openWeatherMapClient")
public class OpenWeatherMapClient implements WeatherClient {
  private static final Logger log = LoggerFactory.getLogger(OpenWeatherMapClient.class);
  private final RestTemplate rest;
  private final String url, apiKey;

  public OpenWeatherMapClient(RestTemplate rest,
                              @Value("${weather.openweathermap.url}") String url,
                              @Value("${weather.openweathermap.apiKey}") String apiKey) {
    this.rest = rest; this.url = url; this.apiKey = apiKey;
  }

  @Override
  public WeatherResponse getWeather(String city) {
    try {
      String uri = String.format("%s?q=%s,AU&appid=%s&units=metric", url, city, apiKey);
      Map<?,?> resp = rest.getForObject(uri, Map.class);
      Map<?,?> main = (Map<?,?>) resp.get("main");
      Map<?,?> wind = (Map<?,?>) resp.get("wind");
      double temp = ((Number) main.get("temp")).doubleValue();
      double windSpeed = ((Number) wind.get("speed")).doubleValue();
      log.info("OpenWeatherMap → {}°C, {} m/s", temp, windSpeed);
      return new WeatherResponse(temp, windSpeed * 3.6);
    } catch (Exception e) {
      throw new ExternalServiceException("OpenWeatherMap failed", e);
    }
  }
}
