package ru.noleg.bishopprototype;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.synthetichumancorestarter.command.model.PriorityType;
import ru.noleg.synthetichumancorestarter.command.model.SyntheticCommand;
import ru.noleg.synthetichumancorestarter.command.api.CommandListenService;
import ru.noleg.synthetichumancorestarter.exception.error.AndroidException;
import ru.noleg.synthetichumancorestarter.exception.error.ErrorCode;

@RestController
@RequestMapping("/commands")
public class CommandController {

    private final CommandListenService commandListenService;
    private final TestService testService;

    public CommandController(CommandListenService commandListenService, TestService testService) {
        this.commandListenService = commandListenService;
        this.testService = testService;
    }

    @PostMapping
    public String executeCommand(@Valid @RequestBody SyntheticCommand command) {
        commandListenService.listen(command);
        if (command.priority() == PriorityType.CRITICAL) {
            throw new AndroidException(ErrorCode.COMMAND_VALIDATION_ERROR, "Critical command");
        }
        return "Command executed";
    }

    @PostMapping("/annotated")
    public String executeAnnotatedCommand(@RequestParam("message") String message) {
        return testService.testMethod(message);
    }

}
