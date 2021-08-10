package be.g00glen00b.apps.medicationassistant.authentication;

class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super(message);
    }
}
