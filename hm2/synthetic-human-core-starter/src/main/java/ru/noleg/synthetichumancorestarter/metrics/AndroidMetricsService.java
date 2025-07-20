package ru.noleg.synthetichumancorestarter.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AndroidMetricsService implements MetricService {
    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> authorCounters = new ConcurrentHashMap<>();
    private final AtomicInteger queueSize = new AtomicInteger(0);

    public AndroidMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        setupMetrics();
    }

    private void setupMetrics() {
        Gauge.builder("android.queue.size", queueSize, AtomicInteger::get)
                .description("Current number of tasks in the queue")
                .register(meterRegistry);

        Counter.builder("android.tasks.total")
                .description("Total tasks processed")
                .register(meterRegistry);
    }

    public void incrementTaskCount(String author) {
        meterRegistry.counter("android.tasks.total").increment();

        authorCounters.computeIfAbsent(author,
                a -> Counter.builder("android.tasks.by.author")
                        .tag("author", a)
                        .description("Tasks processed by author")
                        .register(meterRegistry)
        ).increment();
    }
}