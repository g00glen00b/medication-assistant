package be.g00glen00b.apps.medicationassistant.authentication;

import be.g00glen00b.apps.medicationassistant.core.MessageDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserFacade facade;

    @PostMapping
    @ApiOperation(value = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = UserInfoDTO.class),
        @ApiResponse(code = 400, message = "Bad request", response = MessageDTO.class)
    })
    public UserInfoDTO createUser(@RequestBody CreateUserRequestDTO request) {
        return facade.createUser(request);
    }

    @GetMapping("/current")
    @ApiOperation(value = "Retrieve the currently authenticated user", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = UserInfoDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class),
        @ApiResponse(code = 404, message = "Not found", response = MessageDTO.class)
    })
    public UserInfoDTO findCurrentUser(@AuthenticationPrincipal UserAuthenticationInfoDTO userDetails) {
        return facade
            .findById(userDetails.getId())
            .orElseThrow(() -> new UserNotFoundException("Currently authenticated user no longer exists"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUserException.class)
    public MessageDTO handleInvalidUser(InvalidUserException ex) {
        return new MessageDTO(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public MessageDTO handleInvalidUser(UserNotFoundException ex) {
        return new MessageDTO(ex.getMessage());
    }
}
