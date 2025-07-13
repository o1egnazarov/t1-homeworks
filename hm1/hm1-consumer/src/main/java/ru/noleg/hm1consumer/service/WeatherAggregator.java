package ru.noleg.hm1consumer.service;

import ru.noleg.kerneldata.model.WeatherData;

import java.util.Collection;
import java.util.Map;

public interface WeatherAggregator {
    void aggregate(WeatherData data);

    Map<String, Collection<WeatherData>> getRecentData(); // для аналитики

    Map<String, Integer> getRainyDaysPerWeek();

    Map<String, WeatherData> getHottestDays();
}
