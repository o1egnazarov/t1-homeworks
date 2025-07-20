package ru.noleg.synthetichumancorestarter.command.api;

import ru.noleg.synthetichumancorestarter.command.model.SyntheticCommand;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CommandQueue {
    private final BlockingQueue<SyntheticCommand> queue;

    public CommandQueue(BlockingQueue<SyntheticCommand> queue) {
        this.queue = queue;
    }

    public void add(SyntheticCommand command) {
        if (!queue.offer(command)) {
            throw new IllegalStateException("Queue is full");
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
