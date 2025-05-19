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

@Component("weatherStackClient")
public class WeatherStackClient implements WeatherClient {
  private static final Logger log = LoggerFactory.getLogger(WeatherStackClient.class);
  private final RestTemplate rest;
  private final String url, key;

  public WeatherStackClient(RestTemplate rest,
                            @Value("${weather.stack.url}") String url,
                            @Value("${weather.stack.accessKey}") String key) {
    this.rest = rest;
    this.url = url;
    this.key = key;
  }

  @Override
  public WeatherResponse getWeather(String city) {
    try {
      String uri = String.format("%s?access_key=%s&query=%s", url, key, city);
      Map<?,?> resp = rest.getForObject(uri, Map.class);
      Map<?,?> current = (Map<?,?>) resp.get("current");
      double temp = ((Number) current.get("temperature")).doubleValue();
      double wind = ((Number) current.get("wind_speed")).doubleValue();
      log.info("WeatherStack → {}°C, {} km/h", temp, wind);
      return new WeatherResponse(temp, wind);
    } catch (Exception e) {
      throw new ExternalServiceException("WeatherStack failed", e);
    }
  }
}
