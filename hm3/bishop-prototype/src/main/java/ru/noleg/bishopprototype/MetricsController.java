package ru.noleg.bishopprototype;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.synthetichumancorestarter.metrics.AndroidMetricsService;

import java.util.Map;

@RestController
@RequestMapping("/api/android/metrics")
public class MetricsController {
    
    private final AndroidMetricsService metricsService;
    private final ThreadPoolTaskExecutor executor;

    public MetricsController(AndroidMetricsService metricsService, 
                          ThreadPoolTaskExecutor executor) {
        this.metricsService = metricsService;
        this.executor = executor;
    }

    @GetMapping("/queue-size")
    public int getCurrentQueueSize() {
        return executor.getThreadPoolExecutor().getQueue().size();
    }

    @GetMapping("/author-stats")
    public Map<String, Double> getAuthorStats() {
        return metricsService.getAuthorStatistics();
    }
}