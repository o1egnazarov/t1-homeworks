package ru.noleg.synthetichumancorestarter.audit.api;

public interface AuditPublisher {
    void send(String message);
}
