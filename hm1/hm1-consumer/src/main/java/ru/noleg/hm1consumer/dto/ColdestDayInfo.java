package ru.noleg.hm1consumer.dto;

import java.time.LocalDate;

public record ColdestDayInfo(String city, LocalDate date, double avgTemp) {
}
