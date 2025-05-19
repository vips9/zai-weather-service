package com.example.weather.controller;

import com.example.weather.model.WeatherResponse;
import com.example.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/weather")
public class WeatherController {
  private static final Logger log = LoggerFactory.getLogger(WeatherController.class);
  private final WeatherService service;

  public WeatherController(WeatherService service) {
    this.service = service;
  }

  @GetMapping
  public WeatherResponse get(@RequestParam(defaultValue="Melbourne") String city) {
    log.debug("Request for weather in {}", city);
    return service.getWeather(city);
  }
}
