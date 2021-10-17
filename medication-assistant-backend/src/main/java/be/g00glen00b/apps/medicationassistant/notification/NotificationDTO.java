package be.g00glen00b.apps.medicationassistant.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class NotificationDTO {
    UUID id;
    NotificationType type;
    String message;

    @JsonCreator
    public NotificationDTO(@JsonProperty("id") UUID id, @JsonProperty("type") NotificationType type, @JsonProperty("message") String message) {
        this.id = id;
        this.type = type;
        this.message = message;
    }

    public NotificationDTO(Notification notification) {
        this(notification.getId(), notification.getType(), notification.getMessage());
    }
}
