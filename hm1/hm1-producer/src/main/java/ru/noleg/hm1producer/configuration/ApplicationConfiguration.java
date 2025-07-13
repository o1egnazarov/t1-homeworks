package ru.noleg.hm1producer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Random;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {
    @Bean
    public Random random() {
        return new Random();
    }
}
