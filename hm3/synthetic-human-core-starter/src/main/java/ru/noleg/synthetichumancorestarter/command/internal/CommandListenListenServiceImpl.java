package ru.noleg.synthetichumancorestarter.command.internal;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.noleg.synthetichumancorestarter.command.api.CommandListenService;
import ru.noleg.synthetichumancorestarter.command.api.CommandQueue;
import ru.noleg.synthetichumancorestarter.command.model.PriorityType;
import ru.noleg.synthetichumancorestarter.command.model.SyntheticCommand;

public class CommandListenListenServiceImpl implements CommandListenService {

    private static final Logger logger = LoggerFactory.getLogger(CommandListenListenServiceImpl.class);

    private final Counter commonCommandsCounter = Metrics.counter("command.common");
    private final Counter criticalCommandsCounter = Metrics.counter("command.critical");

    private final CommandQueue queue;

    public CommandListenListenServiceImpl(CommandQueue queue) {
        this.queue = queue;
    }

    @Override
    public void listen(SyntheticCommand command) {
        if (command.priority() == PriorityType.CRITICAL) {
            criticalCommandsCounter.increment();
            logger.info("Critical command: {}", command);
        } else {
            commonCommandsCounter.increment();
            queue.add(command);
        }
    }
}
