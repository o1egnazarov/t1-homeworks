package ru.noleg.synthetichumancorestarter.metrics;

import java.util.Map;

public interface MetricService {
    void recordTaskExecution(String author, long executionTimeMs);
    void recordTaskRejection(String author);
    int getCurrentQueueSize();
    Map<String, Long> getTasksCountByAuthor();
    Map<String, Double> getAverageExecutionTimeByAuthor();
}