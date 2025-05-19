package com.example.weather.exception;

public class ExternalServiceException extends RuntimeException {
  public ExternalServiceException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
