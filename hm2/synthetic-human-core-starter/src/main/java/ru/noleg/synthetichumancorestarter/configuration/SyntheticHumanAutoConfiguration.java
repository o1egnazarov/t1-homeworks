package ru.noleg.synthetichumancorestarter.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import ru.noleg.synthetichumancorestarter.audit.api.AuditPublisher;
import ru.noleg.synthetichumancorestarter.audit.internal.KafkaAuditPublisher;
import ru.noleg.synthetichumancorestarter.audit.internal.WeylandAuditAspect;
import ru.noleg.synthetichumancorestarter.command.api.CommandListenService;
import ru.noleg.synthetichumancorestarter.command.api.CommandQueue;
import ru.noleg.synthetichumancorestarter.command.api.CommandWorkerService;
import ru.noleg.synthetichumancorestarter.command.internal.CommandListenListenServiceImpl;
import ru.noleg.synthetichumancorestarter.command.internal.CommandWorkerServiceImpl;
import ru.noleg.synthetichumancorestarter.exception.handler.AndroidExceptionHandler;
import ru.noleg.synthetichumancorestarter.metrics.AndroidMetricsService;
import ru.noleg.synthetichumancorestarter.metrics.MetricService;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@EnableConfigurationProperties(SyntheticHumanProperties.class)
public class SyntheticHumanAutoConfiguration {

    @Bean
    public AndroidExceptionHandler androidExceptionHandler() {
        return new AndroidExceptionHandler();
    }

    @Bean
    public CommandQueue commandQueue(SyntheticHumanProperties properties) {
        return new CommandQueue(new LinkedBlockingQueue<>(properties.queueCapacity()));
    }

    @Bean
    public CommandListenService commandListenService(CommandQueue commandQueue) {
        return new CommandListenListenServiceImpl(commandQueue);
    }

    @Bean
    public CommandWorkerService commandWorkerService(CommandQueue commandQueue) {
        return new CommandWorkerServiceImpl(commandQueue, Executors.newSingleThreadExecutor());
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "synthetic.human",
            name = "audit-mode",
            havingValue = "KAFKA"
    )
    @ConditionalOnClass(KafkaTemplate.class)
    public AuditPublisher kafkaAuditPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            SyntheticHumanProperties props
    ) {
        return new KafkaAuditPublisher(kafkaTemplate, props.kafkaTopic());
    }

    @Bean
    public MetricService metricService(MeterRegistry meterRegistry) {
        return new AndroidMetricsService(meterRegistry);
    }


    @Bean
    public WeylandAuditAspect weylandWatchingYouAuditAspect(
            @Autowired(required = false) AuditPublisher kafkaAuditPublisher,
            SyntheticHumanProperties syntheticHumanProperties
    ) {
        return new WeylandAuditAspect(kafkaAuditPublisher, syntheticHumanProperties);
    }
}
