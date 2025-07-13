package ru.noleg.hm1consumer.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.noleg.hm1consumer.service.WeatherAggregator;
import ru.noleg.hm1consumer.service.WeatherConsumer;
import ru.noleg.kerneldata.model.WeatherData;

@Component
public class WeatherConsumerImpl implements WeatherConsumer {

    private static final Logger logger = LoggerFactory.getLogger(WeatherConsumerImpl.class);

    private final WeatherAggregator weatherAggregator;

    public WeatherConsumerImpl(WeatherAggregator weatherAggregator) {
        this.weatherAggregator = weatherAggregator;
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.name}")
    public void listen(WeatherData weatherData) {
        logger.info("Received weather data: {}", weatherData);
        weatherAggregator.aggregate(weatherData);
    }
}
