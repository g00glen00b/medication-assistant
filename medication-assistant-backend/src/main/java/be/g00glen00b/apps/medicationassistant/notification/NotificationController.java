package be.g00glen00b.apps.medicationassistant.notification;

import be.g00glen00b.apps.medicationassistant.authentication.UserAuthenticationInfoDTO;
import be.g00glen00b.apps.medicationassistant.core.MessageDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
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
class NotificationController {
    private final NotificationFacade facade;

    @GetMapping
    @ApiOperation(value = "Retrieve a list of the current authenticated user their notifications", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = NotificationDTOPage.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public Page<NotificationDTO> findAll(Pageable pageable, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return facade.findAll(authentication.getId(), pageable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a notification", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Success", response = NotificationDTOPage.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class),
        @ApiResponse(code = 404, message = "Not found", response = MessageDTO.class)
    })
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        facade.delete(id, authentication.getId());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotificationNotFoundException.class)
    public MessageDTO handleNotFoundException(NotificationNotFoundException ex) {
        return new MessageDTO(ex.getMessage());
    }

    private interface NotificationDTOPage extends Page<NotificationDTO> {}
}
