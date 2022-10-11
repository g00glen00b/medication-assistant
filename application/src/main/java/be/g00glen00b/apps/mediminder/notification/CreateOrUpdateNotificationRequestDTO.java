package be.g00glen00b.apps.mediminder.notification;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record CreateOrUpdateNotificationRequestDTO(
    @NotNull(message = "{notification.type.notNull}")
    NotificationType type,
    @NotNull(message = "{notification.reference.notNull}")
    @NotEmpty(message = "{notification.reference.notNull}")
    @Size(max = 128, message = "{notification.reference.size}")
    String reference,
    @NotNull(message = "{notification.message.notNull}")
    @NotEmpty(message = "{notification.message.notNull}")
    @Size(max = 512, message = "{notification.message.size}")
    String message) {
}
