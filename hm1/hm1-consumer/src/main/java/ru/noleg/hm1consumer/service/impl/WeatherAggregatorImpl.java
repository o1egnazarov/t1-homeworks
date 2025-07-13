package ru.noleg.hm1consumer.service.impl;

import com.google.common.collect.EvictingQueue;
import org.springframework.stereotype.Service;
import ru.noleg.hm1consumer.service.WeatherAggregator;
import ru.noleg.kerneldata.model.WeatherCondition;
import ru.noleg.kerneldata.model.WeatherData;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherAggregatorImpl implements WeatherAggregator {

    private static final int MAX_RECORDS_PER_CITY = 100;

    private final Map<String, EvictingQueue<WeatherData>> recentDataByCity = new ConcurrentHashMap<>();
    private final Map<String, Integer> rainyDaysPerWeek = new ConcurrentHashMap<>();
    private final Map<String, WeatherData> hottestDay = new ConcurrentHashMap<>();

    @Override
    public void aggregate(WeatherData data) {
        String city = getCityName(data);

        recentDataByCity.computeIfAbsent(city, c -> EvictingQueue.create(MAX_RECORDS_PER_CITY)).add(data);

        hottestDay.merge(city, data, (oldData, newData) ->
                newData.temperature() > oldData.temperature() ? newData : oldData
        );

        if (data.condition() == WeatherCondition.RAINY) {
            int week = getWeekOfYear(data.date());
            String key = city + ":" + week;
            rainyDaysPerWeek.merge(key, 1, Integer::sum);
        }
    }

    private String getCityName(WeatherData data) {
        return data.city().name();
    }

    private int getWeekOfYear(LocalDate date) {
        return date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

    @Override
    public Map<String, Collection<WeatherData>> getRecentData() {
        return Collections.unmodifiableMap(recentDataByCity);
    }

    @Override
    public Map<String, Integer> getRainyDaysPerWeek() {
        return Collections.unmodifiableMap(rainyDaysPerWeek);
    }

    @Override
    public Map<String, WeatherData> getHottestDays() {
        return Collections.unmodifiableMap(hottestDay);
    }
}
