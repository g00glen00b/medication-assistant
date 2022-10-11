package be.g00glen00b.apps.mediminder.notification;

import be.g00glen00b.apps.mediminder.user.UserAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationFacade facade;

    @Operation(summary = "Retrieve a user their notifications", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = NotificationDTOPage.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
    })
    @GetMapping
    public Page<NotificationDTO> findAll(@AuthenticationPrincipal UserAuthentication authentication, Pageable pageable) {
        UUID userId = authentication.getId();
        return facade.findAll(userId, pageable);
    }

    @Operation(summary = "Update an existing scheduled medication intake for a user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        facade.delete(userId, id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotificationNotFoundException.class)
    public MessageDTO handleNotificationNotFound(NotificationNotFoundException ex) {
        return new MessageDTO(ex.getMessage());
    }

    private interface NotificationDTOPage extends Page<NotificationDTO> {}
}
