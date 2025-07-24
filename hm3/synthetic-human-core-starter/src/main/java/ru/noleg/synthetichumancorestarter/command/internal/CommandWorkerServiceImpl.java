package ru.noleg.synthetichumancorestarter.command.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.noleg.synthetichumancorestarter.command.api.CommandQueue;
import ru.noleg.synthetichumancorestarter.command.api.CommandWorkerService;
import ru.noleg.synthetichumancorestarter.command.model.SyntheticCommand;

import java.util.concurrent.ExecutorService;

public class CommandWorkerServiceImpl implements CommandWorkerService {

    private static final Logger logger = LoggerFactory.getLogger(CommandWorkerServiceImpl.class);

    private final CommandQueue queue;
    private final ExecutorService workerPool;

    public CommandWorkerServiceImpl(CommandQueue queue, ExecutorService workerPool) {
        this.queue = queue;
        this.workerPool = workerPool;
        execute();
    }

    @Override
    public void execute() {
        workerPool.submit(() -> {
            while (true) {
                try {
                    SyntheticCommand command = queue.take();

                    logger.info("Common command: {}", command);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
