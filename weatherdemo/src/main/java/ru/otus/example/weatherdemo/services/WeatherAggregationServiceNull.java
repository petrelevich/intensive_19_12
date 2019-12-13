package ru.otus.example.weatherdemo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.example.weatherdemo.models.Weather;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("weatherServiceNull")
public class WeatherAggregationServiceNull implements WeatherServiceAggregation {
    private static Logger logger = LoggerFactory.getLogger(WeatherAggregationServiceNull.class);

    private final List<WeatherService> weatherServices;
    private final WeatherCache weatherCache;
    private final BlockingQueue<List<Weather>> weatherQueue;
    private final ExecutorService executor;
    private static final List<Weather> EMPTY_LIST = new ArrayList<>();

    public WeatherAggregationServiceNull(List<WeatherService> weatherServices, WeatherCache weatherCache) {
        this.weatherServices = weatherServices;
        this.weatherCache = weatherCache;
        weatherQueue = new ArrayBlockingQueue<>(weatherServices.size());
        executor = Executors.newFixedThreadPool(weatherServices.size());
    }

    @Override
    public List<Weather> getWeather() {
        try {
            List<Weather> weatherList = weatherCache.getValueNull();
            if (weatherList == null) {
                var weather = doRequest();
                weatherCache.putValue(weather);
            }
            return weatherList;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return EMPTY_LIST;
        }


    }

    private List<Weather> doRequest() throws InterruptedException {
        weatherQueue.clear();
        for (WeatherService weatherService : weatherServices) {
            executor.submit(() -> {
                var result = weatherQueue.offer(weatherService.getWeather());
                logger.debug("weatherQueue.offer result:{}", result);
            });
        }
        return weatherQueue.take();
    }

    @PreDestroy
    public void destroy() {
        executor.shutdownNow();
    }
}
