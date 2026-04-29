package ru.job4j.api.validation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "Validation violation details")
public class Violation {

    @Schema(description = "Name of the field that failed validation", example = "username")
    private final String fieldName;

    @Schema(description = "Validation error message", example = "username не может быть пустым")
    private final String message;
}
