package ru.noleg.hm1producer.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfiguration {

    @Value("${kafka.weather-topic.name}")
    private String topicName;

    @Value("${kafka.weather-topic.num-partitions}")
    private int partitions;

    @Value("${kafka.weather-topic.num-replicas}")
    private int replicas;

    @Bean
    public NewTopic weatherTopic() {
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
