package ru.otus.example.weatherdemo.services;

import java.util.Optional;

public interface Cache<T> {

    void putValue(T value);
    Optional<T> getValue();
    T getValueNull();
}
