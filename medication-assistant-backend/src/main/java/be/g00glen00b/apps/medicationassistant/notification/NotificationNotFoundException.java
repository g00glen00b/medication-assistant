package be.g00glen00b.apps.medicationassistant.notification;

class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String message) {
        super(message);
    }
}
