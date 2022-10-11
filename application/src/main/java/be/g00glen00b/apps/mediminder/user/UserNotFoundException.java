package be.g00glen00b.apps.mediminder.user;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class UserNotFoundException extends RuntimeException {
    private final UUID id;

    @Override
    public String getMessage() {
        return "User with ID '" + id + "' was not found";
    }
}
