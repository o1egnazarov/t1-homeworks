package ru.noleg.hm1producer.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.noleg.hm1producer.service.WeatherProducer;
import ru.noleg.hm1producer.service.WeatherScheduler;

import java.util.concurrent.TimeUnit;

@Component
public class WeatherSchedulerImpl implements WeatherScheduler {

    private final WeatherProducer producer;
    private final WeatherDataGeneratorImpl generator;

    public WeatherSchedulerImpl(WeatherProducer producer, WeatherDataGeneratorImpl generator) {
        this.producer = producer;
        this.generator = generator;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void generateAndSend() {
        producer.send(generator.generate());
    }
}