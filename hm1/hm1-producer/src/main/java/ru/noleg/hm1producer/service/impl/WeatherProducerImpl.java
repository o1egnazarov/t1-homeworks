package ru.noleg.hm1producer.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.noleg.hm1producer.service.WeatherProducer;
import ru.noleg.kerneldata.model.WeatherData;

@Component
public class WeatherProducerImpl implements WeatherProducer {

    private static final Logger logger = LoggerFactory.getLogger(WeatherProducerImpl.class);

    @Value("${kafka.weather-topic.name}")
    private String topic;
    private final KafkaTemplate<String, WeatherData> kafkaTemplate;

    public WeatherProducerImpl(KafkaTemplate<String, WeatherData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(WeatherData weatherData) {
        kafkaTemplate.send(topic, weatherData.city().toString(), weatherData);
        logger.info("Sent weather data for city: {}", weatherData.city());
    }
}
