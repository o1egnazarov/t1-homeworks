package ru.noleg.synthetichumancorestarter.command.api;

import ru.noleg.synthetichumancorestarter.command.model.SyntheticCommand;
import ru.noleg.synthetichumancorestarter.exception.error.AndroidException;
import ru.noleg.synthetichumancorestarter.exception.error.ErrorCode;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CommandQueue {
    private final BlockingQueue<SyntheticCommand> queue;

    public CommandQueue(BlockingQueue<SyntheticCommand> queue) {
        this.queue = queue;
    }

    public void add(SyntheticCommand command) {
        if (!queue.offer(command)) {
            throw new AndroidException(ErrorCode.QUEUE_OVERFLOW, "Queue is full, max size: " + size());
        }
    }

    public SyntheticCommand take() throws InterruptedException {
        return queue.take();
    }

    public int size() {
        return queue.size();
    }

    public List<SyntheticCommand> snapshot() {
        return List.copyOf(queue);
    }
}
