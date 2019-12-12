package ru.otus.example.weatherdemo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.example.weatherdemo.models.Weather;
import ru.otus.example.weatherdemo.services.WeatherAggregationService;
import ru.otus.example.weatherdemo.services.WeatherServiceAggregation;

import java.util.List;

@RestController
public class WeatherRestController {
    private static Logger logger = LoggerFactory.getLogger(WeatherAggregationService.class);

    private final WeatherServiceAggregation weatherService;
    private final WeatherServiceAggregation weatherServiceNull;

    public WeatherRestController(@Qualifier("weatherService") WeatherServiceAggregation weatherService,
                                 @Qualifier("weatherServiceNull") WeatherServiceAggregation weatherServiceNull) {
        this.weatherService = weatherService;
        this.weatherServiceNull = weatherServiceNull;
    }

    @GetMapping("api/weather")
    public List<Weather> getWeather() {
        logger.info("get weather");
        return weatherService.getWeather();
    }

    @GetMapping("api/weatherNull")
    public List<Weather> getWeatherNull() {
        logger.info("get weatherNull");
        return weatherServiceNull.getWeather();
    }

}
