package be.g00glen00b.apps.medicationassistant.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class CreateNotificationRequestDTO {
    UUID userId;
    UUID relevantId;
    NotificationType type;
    String message;

    @JsonCreator
    public CreateNotificationRequestDTO(
        @JsonProperty("userId") UUID userId,
        @JsonProperty("relevantId") UUID relevantId,
        @JsonProperty("type") NotificationType type,
        @JsonProperty("message") String message) {
        this.userId = userId;
        this.relevantId = relevantId;
        this.type = type;
        this.message = message;
    }

    public CreateNotificationRequestDTO(Notification notification) {
        this(notification.getUserId(), notification.getRelevantId(), notification.getType(), notification.getMessage());
    }

    public boolean isUniqueNotification() {
        return relevantId != null;
    }
}
