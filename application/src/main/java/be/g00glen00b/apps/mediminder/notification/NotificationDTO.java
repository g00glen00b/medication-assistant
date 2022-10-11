package be.g00glen00b.apps.mediminder.notification;

import be.g00glen00b.apps.mediminder.notification.implementation.NotificationEntity;

import java.time.Instant;
import java.util.UUID;

public record NotificationDTO(UUID id, String message, NotificationType type, Instant createdDate) {
    public static NotificationDTO ofEntity(NotificationEntity entity) {
        return new NotificationDTO(entity.getId(), entity.getMessage(), entity.getType(), entity.getCreatedDate());
    }
}
