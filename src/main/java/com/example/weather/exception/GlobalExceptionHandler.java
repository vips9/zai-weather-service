package com.example.weather.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ExternalServiceException.class)
  public ResponseEntity<String> handleExternal(ExternalServiceException ex) {
    log.error("External API failure: {}", ex.getMessage());
    return ResponseEntity.status(503)
                         .body("Weather providers are unavailable. Please try again later.");
  }
}
