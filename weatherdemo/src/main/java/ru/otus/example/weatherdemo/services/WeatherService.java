package ru.otus.example.weatherdemo.services;

import ru.otus.example.weatherdemo.models.Weather;

import java.util.List;

public interface WeatherService {
    List<Weather> getWeather();
}
