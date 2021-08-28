package be.g00glen00b.apps.medicationassistant.authentication;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
