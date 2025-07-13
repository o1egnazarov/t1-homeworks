package ru.noleg.kerneldata.model;

import java.time.LocalDate;

public record WeatherData(
        City city,
        LocalDate date,
        int temperature,
        int windSpeed,
        int pressure,
        int humidity,
        WeatherCondition condition
) {
}
