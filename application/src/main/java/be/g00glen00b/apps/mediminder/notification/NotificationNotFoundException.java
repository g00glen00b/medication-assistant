package be.g00glen00b.apps.mediminder.notification;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class NotificationNotFoundException extends RuntimeException {
    private final UUID id;

    @Override
    public String getMessage() {
        return "Notification with ID '" + id + "' was not found";
    }
}
