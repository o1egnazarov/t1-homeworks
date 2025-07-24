package ru.noleg.synthetichumancorestarter.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class AndroidMetricServiceImpl implements MetricService {

    private final MeterRegistry meterRegistry;

    private final ConcurrentMap<String, LongAdder> authorTaskCounters = new ConcurrentHashMap<>();
    private final LongAdder totalTasks = new LongAdder();
    private final LongAdder rejectedTasks = new LongAdder();

    public AndroidMetricServiceImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        Gauge.builder("android.queue.size", this::getCurrentQueueSize)
                .description("Current number of tasks in queue")
                .register(meterRegistry);


//        meterRegistry.gaugeMapSize("android.authors.count", authorTaskCounters);
    }

    @Override
    public void recordTaskExecution(String author, long executionTimeMs) {
        // Обновляем счетчики
        authorTaskCounters.computeIfAbsent(author, k -> new LongAdder()).increment();
        totalTasks.increment();

        // Записываем метрики
        Counter.builder("android.tasks.executed")
                .tag("author", author)
                .register(meterRegistry)
                .increment();

        Timer.builder("android.task.execution.time")
                .tag("author", author)
                .register(meterRegistry)
                .record(executionTimeMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void recordTaskRejection(String author) {
        rejectedTasks.increment();
        Counter.builder("android.tasks.rejected")
                .tag("author", author)
                .register(meterRegistry)
                .increment();
    }

    @Override
    public int getCurrentQueueSize() {
        return 0;
    }

    @Override
    public Map<String, Long> getTasksCountByAuthor() {
        return authorTaskCounters.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().longValue()
                ));
    }

    @Override
    public Map<String, Double> getAverageExecutionTimeByAuthor() {
        // Реализация через MeterRegistry
        return meterRegistry.find("android.task.execution.time").timers().stream()
                .collect(Collectors.toMap(
                        t -> t.getId().getTag("author"),
                        t -> t.mean(TimeUnit.MILLISECONDS)
                ));
    }
}