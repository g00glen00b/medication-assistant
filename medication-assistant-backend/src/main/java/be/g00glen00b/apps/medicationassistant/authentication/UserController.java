package be.g00glen00b.apps.medicationassistant.authentication;

import be.g00glen00b.apps.medicationassistant.core.MessageDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ApiOperation(value = "")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = UserInfoDTO.class),
        @ApiResponse(code = 400, message = "Bad request", response = MessageDTO.class)
    })
    public UserInfoDTO createUser(@RequestBody CreateUserRequestDTO request) {
        return service.createUser(request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUserException.class)
    public MessageDTO handleInvalidUser(InvalidUserException ex) {
        return new MessageDTO(ex.getMessage());
    }
}
