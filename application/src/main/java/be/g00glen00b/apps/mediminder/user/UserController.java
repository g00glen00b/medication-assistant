package be.g00glen00b.apps.mediminder.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserFacade userFacade;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @PostMapping
    public UserInfoDTO createUser(@RequestBody CreateUserRequestDTO request) {
        return userFacade.create(request);
    }

    @Operation(summary = "Update a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @PutMapping
    public UserInfoDTO updateUser(@RequestBody UpdateUserRequestDTO request, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        return userFacade.update(userId, request);
    }

    @Operation(summary = "Update the credentials of a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @PutMapping("/credentials")
    public UserInfoDTO updateCredentials(@RequestBody UpdateCredentialsRequestDTO request, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        return userFacade.updateCredentials(userId, request);
    }

    @Operation(summary = "Retrieve all possible timezones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = String[].class)))
    })
    @GetMapping("/timezone")
    public Collection<String> findAllTimezones() {
        return userFacade.findAvailableTimezones();
    }

    @Operation(summary = "Check if a user already exists with a given email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    @GetMapping("/email/exists")
    public boolean existsUser(@RequestParam String email) {
        return userFacade.existsByEmail(email);
    }

    @Operation(summary = "Retrieve information of the current user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @GetMapping("/current")
    public UserInfoDTO findCurrentUser(@AuthenticationPrincipal UserAuthentication userDetails, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        csrfToken.getToken();
        return userFacade.findById(userDetails.getId());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUserException.class)
    public MessageDTO handleInvalidUser(InvalidUserException ex) {
        return new MessageDTO(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public MessageDTO handleUserNotFound(UserNotFoundException ex) {
        return new MessageDTO(ex.getMessage());
    }
}
