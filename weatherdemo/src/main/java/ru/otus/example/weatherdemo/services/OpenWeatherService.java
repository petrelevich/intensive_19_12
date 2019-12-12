package ru.otus.example.weatherdemo.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.example.weatherdemo.models.Weather;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class OpenWeatherService implements WeatherService {
    private static Logger logger = LoggerFactory.getLogger(OpenWeatherService.class);

    private final RestTemplate restTemplate;


    public OpenWeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${app.openweather-api-key}")
    private String apiKey;

    @Value("${app.city-name}")
    private String cityName;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Weather.class, new JsonDeserializer<Weather>() {
        @Override
        public Weather deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
            JsonObject main = jsonElement.getAsJsonObject().getAsJsonObject("main");
            return new Weather("OpenWeatherMap", cityName, main.get("temp").getAsString());
        }
    }).create();

    @Override
    public List<Weather> getWeather() {
        logger.info("Open performing request...");
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=ru&appid=%s", cityName, apiKey);
        String weatherString = restTemplate.getForObject(url, String.class);
        logger.info("Open request done.");
        return List.of(gson.fromJson(weatherString, Weather.class));
    }

}
