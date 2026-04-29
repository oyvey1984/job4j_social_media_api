package ru.job4j.api.validation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "Validation error response containing all violations")
public class ValidationErrorResponse {

    @Schema(description = "List of validation violations")
    private final List<Violation> violations;
}
