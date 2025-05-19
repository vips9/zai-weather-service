# Zai Weather Service

A Spring Boot (2.7.x) microservice that returns unified weather data for Melbourne,
using WeatherStack (primary) with OpenWeatherMap as failover, Caffeine cache,
 logging and exception handling.

## Prerequisites

* Java 8
* Maven 3.6+
* Environment vars:

  * `WEATHERSTACK_KEY` → your WeatherStack API key
  * `OPENWEATHERMAP_KEY` → your OpenWeatherMap API key

## Build & Run Locally

```bash
git clone https://github.com/vips9/zai-weather-service.git
cd zai-weather-service
mvn clean package
export WEATHERSTACK_KEY=…
export OPENWEATHERMAP_KEY=…
java -jar target/zai-weather-service-1.0.0.jar
```

Service listens on port 8080:

```bash
curl "http://localhost:8080/v1/weather?city=Melbourne"
# → {"temperatureDegrees":29.0,"windSpeed":20.0}
```

