package ru.noleg.hm1producer.service;


import ru.noleg.kerneldata.model.WeatherData;

public interface WeatherProducer {
    void send(WeatherData weatherData);
}
