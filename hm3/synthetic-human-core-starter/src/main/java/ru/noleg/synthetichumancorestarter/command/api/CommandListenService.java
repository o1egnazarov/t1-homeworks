package ru.noleg.synthetichumancorestarter.command.api;

import ru.noleg.synthetichumancorestarter.command.model.SyntheticCommand;

public interface CommandListenService {
    void listen(SyntheticCommand command);
}
