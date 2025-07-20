package ru.noleg.synthetichumancorestarter.audit.internal;

import org.springframework.kafka.core.KafkaTemplate;
import ru.noleg.synthetichumancorestarter.audit.api.AuditPublisher;

public class KafkaAuditPublisher implements AuditPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaAuditPublisher(KafkaTemplate<String, String> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(String message) {
        kafkaTemplate.send(topic, message);
    }
}