package ru.noleg.synthetichumancorestarter.command.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SyntheticCommand(
        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description is too long, max 1000 symbols")
        String description,

        @NotNull(message = "Priority is required")
        PriorityType priority,

        @NotBlank(message = "Author is required")
        @Size(max = 100, message = "Author is too long, max 100 symbols")
        String author,

        @NotBlank(message = "Time is required")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*$", message = "Invalid time format")
        String time
) {
}
