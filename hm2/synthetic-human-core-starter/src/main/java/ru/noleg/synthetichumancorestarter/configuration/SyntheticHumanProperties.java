package ru.noleg.synthetichumancorestarter.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import ru.noleg.synthetichumancorestarter.audit.model.AuditMode;

@Validated
@ConditionalOnProperty(prefix = "synthetic.human", name = {"queue-capacity", "audit-mode"})
@ConfigurationProperties(prefix = "synthetic.human")
public record SyntheticHumanProperties(
        @NotNull(message = "Queue capacity must be set") Integer queueCapacity,
        @NotNull(message = "Audit mode must be set") AuditMode auditMode,
        String kafkaTopic) {
}