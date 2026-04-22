package ru.job4j.api.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {
    private final List<Violation> violations;
}
