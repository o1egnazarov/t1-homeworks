package ru.noleg.hm1consumer.service;

import org.springframework.stereotype.Service;
import ru.noleg.hm1consumer.dto.ColdestDayInfo;
import ru.noleg.hm1consumer.dto.HottestDayInfo;
import ru.noleg.hm1consumer.dto.WeatherAnalytics;
import ru.noleg.kerneldata.model.WeatherData;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeatherAnalyticsServiceImpl implements WeatherAnalyticsService {

    private final WeatherAggregator aggregator;

    public WeatherAnalyticsServiceImpl(WeatherAggregator aggregator) {
        this.aggregator = aggregator;
    }

    @Override
    public WeatherAnalytics getAnalytics() {
        var recentData = aggregator.getRecentData();
        var hottestDays = aggregator.getHottestDays();
        var rainyDays = aggregator.getRainyDaysPerWeek();

        String rainiestCityWeek = getRainiestCityWeek(rainyDays);
        Map<String, HottestDayInfo> hottestDaysByCity = getStringHottestDaysInfoMap(hottestDays);
        ColdestDayInfo coldest = getColdestDayInfo(recentData);

        return new WeatherAnalytics(
                rainiestCityWeek,
                hottestDaysByCity,
                coldest
        );
    }

    private ColdestDayInfo getColdestDayInfo(Map<String, Collection<WeatherData>> recentData) {
        return recentData.entrySet().stream()
                .map(entry -> {
                    String city = entry.getKey();
                    Collection<WeatherData> data = entry.getValue();

                    double avg = data.stream().mapToInt(WeatherData::temperature)
                            .average().orElse(Double.NaN);

                    LocalDate coldestDate = data.stream()
                            .min(Comparator.comparingInt(WeatherData::temperature))
                            .map(WeatherData::date)
                            .orElse(null);

                    return new ColdestDayInfo(city, coldestDate, avg);
                })
                .filter(info -> !Double.isNaN(info.avgTemp()))
                .min(Comparator.comparingDouble(ColdestDayInfo::avgTemp))
                .orElse(null);
    }

    private Map<String, HottestDayInfo> getStringHottestDaysInfoMap(Map<String, WeatherData> hottestDays) {
        return hottestDays.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            WeatherData weatherData = entry.getValue();
                            return new HottestDayInfo(
                                    weatherData.date(),
                                    weatherData.temperature(),
                                    weatherData.condition()
                            );
                        }
                ));
    }

    private String getRainiestCityWeek(Map<String, Integer> rainyDays) {
        return rainyDays.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}