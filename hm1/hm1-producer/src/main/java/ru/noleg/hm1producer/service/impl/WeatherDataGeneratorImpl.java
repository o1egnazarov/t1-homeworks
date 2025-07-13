package ru.noleg.hm1producer.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.noleg.hm1producer.service.WeatherGenerator;
import ru.noleg.kerneldata.model.City;
import ru.noleg.kerneldata.model.WeatherCondition;
import ru.noleg.kerneldata.model.WeatherData;

import java.time.LocalDate;
import java.util.Random;

@Component
public class WeatherDataGeneratorImpl implements WeatherGenerator {

    private static final Logger logger = LoggerFactory.getLogger(WeatherDataGeneratorImpl.class);

    private static final int DAYS_IN_WEEK = 7;

    private static final int MAX_TEMPERATURE = 36;

    private static final int MIN_HUMIDITY = 30;
    private static final int MAX_HUMIDITY = 60;

    private static final int MIN_WIND_SPEED = 5;
    private static final int MIN_WIND_SPEED_WHEN_WINDY = 10;

    private static final int MIN_PRESSURE = 730;

    private final Random random;

    public WeatherDataGeneratorImpl(Random random) {
        this.random = random;
    }

    public WeatherData generate() {
        City city = City.values()[random.nextInt(City.values().length)];
        LocalDate date = LocalDate.now().plusDays(random.nextInt(DAYS_IN_WEEK));

        WeatherCondition condition = WeatherCondition.values()[random.nextInt(WeatherCondition.values().length)];
        int temperature = random.nextInt(MAX_TEMPERATURE);
        int humidity = random.nextInt(MAX_HUMIDITY) + MIN_HUMIDITY;
        int windSpeed = condition == WeatherCondition.WINDY ?
                random.nextInt(5) + MIN_WIND_SPEED_WHEN_WINDY :
                random.nextInt(MIN_WIND_SPEED);
        int pressure = random.nextInt(50) + MIN_PRESSURE;

        WeatherData generatedWeatherData =
                new WeatherData(city, date, temperature, windSpeed, pressure, humidity, condition);

        logger.info("Generated weather data: {}", generatedWeatherData);
        return generatedWeatherData;
    }
}
