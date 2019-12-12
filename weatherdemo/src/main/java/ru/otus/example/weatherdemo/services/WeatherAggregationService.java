package ru.otus.example.weatherdemo.services;

import org.springframework.stereotype.Service;
import ru.otus.example.weatherdemo.models.Weather;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("weatherService")
public class WeatherAggregationService implements WeatherServiceAggregation {
    private static Logger logger = LoggerFactory.getLogger(WeatherAggregationService.class);

    private final List<WeatherService> weatherServices;
    private final WeatherCache weatherCache;
    private static final List<Weather> EMPTY_LIST = new ArrayList<>();

    public WeatherAggregationService(List<WeatherService> weatherServices, WeatherCache weatherCache) {
        this.weatherServices = weatherServices;
        this.weatherCache = weatherCache;
    }

    @Override
    public List<Weather> getWeather() {
        return weatherCache.getValue().orElseGet(() -> {
            var weather = doRequest();
            weatherCache.putValue(weather);
            return weather;
        });
    }

    private List<Weather> doRequest() {
        CompletableFuture[] weatherFutures = new CompletableFuture[weatherServices.size()];

        int idx = 0;
        for (WeatherService weatherService : weatherServices) {
            weatherFutures[idx++] = CompletableFuture.supplyAsync(weatherService::getWeather);
        }

        try {
            CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(weatherFutures);
            return (List<Weather>) combinedFuture.get(30, TimeUnit.SECONDS);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return EMPTY_LIST;
        }
    }
}
