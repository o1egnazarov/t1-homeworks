package ru.noleg.hm1consumer.service;


import ru.noleg.kerneldata.model.WeatherData;

public interface WeatherConsumer {
    void listen(WeatherData weatherData);
}
