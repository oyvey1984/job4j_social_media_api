package ru.job4j.api.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.api.dto.UserDTO;
import ru.job4j.api.validation.ValidationErrorResponse;

import java.util.List;

@Tag(name = "UserController", description = "UserController management APIs")
@RequestMapping("/api/users")
public interface UserControllerApi {

    @Operation(
            summary = "Retrieve all users",
            description = "Returns a list of all registered users. The response includes id, username and email for each user.",
            tags = { "UserController" }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of users retrieved successfully",
                    content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - invalid parameters or validation failed",
                    content = { @Content(schema = @Schema(implementation = ValidationErrorResponse.class), mediaType = "application/json") }
            )
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll();

    @Operation(
            summary = "Retrieve a User by userId",
            description = "Get a User object by specifying its userId. The response is User object with userId, username and date of created.",
            tags = { "UserController"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid userId format (must be >= 1)",
                    content = { @Content(schema = @Schema()) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User with specified ID not found",
                    content = { @Content(schema = @Schema()) }
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> get(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable("userId")
            @NotNull
            @Min(value = 1, message = "номер ресурса должен быть 1 и более")
            Long userId);

    @Operation(
            summary = "Create a new User",
            description = "Creates a new user with the provided data. Returns the created user with generated ID.",
            tags = { "UserController" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid input data (validation failed)",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "409", description = "User with this email already exists",
                    content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody @Valid UserDTO userDTO);

    @Operation(
            summary = "Update an existing User",
            description = "Fully updates an existing user. Requires all fields to be provided. The user ID must exist.",
            tags = { "UserController" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User updated successfully (no content returned)",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data or missing user ID",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "User with specified ID not found",
                    content = { @Content(schema = @Schema()) })
    })
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserDTO userDTO);

    @Operation(
            summary = "Partially update an existing User",
            description = "Updates only the provided fields of an existing user. User ID is required.",
            tags = { "UserController" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User partially updated successfully",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "User with specified ID not found",
                    content = { @Content(schema = @Schema()) })
    })
    @PatchMapping
    public ResponseEntity<Void> change(@RequestBody @Valid UserDTO userDTO);

    @Operation(
            summary = "Delete a User by userId",
            description = "Deletes an existing user. The user ID must be valid and exist in the database.",
            tags = { "UserController" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully (no content returned)",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Invalid userId format (must be >= 1)",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "User with specified ID not found",
                    content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeById(@PathVariable("userId")
                                           @NotNull
                                           @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                           Long userId);
}
