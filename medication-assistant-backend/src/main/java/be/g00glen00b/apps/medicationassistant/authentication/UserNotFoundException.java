package be.g00glen00b.apps.medicationassistant.authentication;

class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
