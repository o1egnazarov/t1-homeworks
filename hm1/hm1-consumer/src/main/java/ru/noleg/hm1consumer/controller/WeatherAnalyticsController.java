package ru.noleg.hm1consumer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.hm1consumer.dto.WeatherAnalytics;
import ru.noleg.hm1consumer.service.WeatherAnalyticsService;

@RestController
@RequestMapping("/analytics")
public class WeatherAnalyticsController {

    private final WeatherAnalyticsService weatherAnalyticsService;

    public WeatherAnalyticsController(WeatherAnalyticsService weatherAnalyticsService) {
        this.weatherAnalyticsService = weatherAnalyticsService;
    }

    @GetMapping()
    public ResponseEntity<WeatherAnalytics> getAnalytics() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(weatherAnalyticsService.getAnalytics());
    }
}
