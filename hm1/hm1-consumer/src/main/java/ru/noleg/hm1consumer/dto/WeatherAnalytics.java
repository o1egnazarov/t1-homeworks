package ru.noleg.hm1consumer.dto;

import java.util.Map;

public record WeatherAnalytics(
        String rainiestCityWeek,
        Map<String, HottestDayInfo> hottestDaysByCity,
        ColdestDayInfo coldestDayInfo
) {
}