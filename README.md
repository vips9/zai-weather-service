# Zai Weather Service

A small Spring Boot (2.7.x) microservice that returns Melbourne’s temperature (°C) and wind speed (km/h).  
It uses WeatherStack first, falls back to OpenWeatherMap on failure, caches results for 3 s, and will serve the last-known reading if both APIs are down.

---

## Prerequisites

- **Java 8**
- **Maven 3.6+**
- Two environment variables with your API keys:
  ```bash
  export WEATHERSTACK_KEY=<your WeatherStack key>
  export OPENWEATHERMAP_KEY=<your OpenWeatherMap key>
  ```

---

## Build & Run

```bash
git clone https://github.com/vips9/zai-weather-service.git
cd zai-weather-service

# Package the app
mvn clean package

# Run the jar
java -jar target/zai-weather-service-1.0.0.jar
```

The service will listen on port **8080**.

---

## Usage

- **Default (Melbourne)**
  ```bash
  curl http://localhost:8080/v1/weather
  # → {"temperatureDegrees":29.0,"windSpeed":20.0}
  ```

- **Specify city**
  ```bash
  curl "http://localhost:8080/v1/weather?city=Sydney"
  ```

---

## Features

- **Primary → fallback** logic between two weather APIs
- ** in-memory cache** (Caffeine) to reduce external calls
- **Stale data** served if both services are down
- **Unit tests** covering success, fallback
- **Clear logging** and 503-style error handling via a global exception handler

---

## Testing

```bash
mvn test
```

You’ll see tests for:
- successful primary fetch
- fallback to the secondary API
- stale-map return when both fail

---

## Future Improvements

- Add Resilience4j circuit breakers and retry policies
- Metrics & health checks (Prometheus, Grafana, Actuator)
- Swagger/OpenAPI UI for interactive docs  
