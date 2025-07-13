package ru.noleg.hm1consumer.dto;

import ru.noleg.kerneldata.model.WeatherCondition;

import java.time.LocalDate;

public record HottestDayInfo(LocalDate date, int temperature, WeatherCondition condition) {
}
