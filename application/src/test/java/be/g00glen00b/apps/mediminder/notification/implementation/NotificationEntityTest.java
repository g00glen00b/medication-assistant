package be.g00glen00b.apps.mediminder.notification.implementation;

import be.g00glen00b.apps.mediminder.notification.NotificationType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationEntityTest {
    @Test
    void of_createsEntity() {
        UUID userId = UUID.randomUUID();
        NotificationEntity entity = NotificationEntity.of(userId, "REF-123", "Message", NotificationType.WARNING);
        assertThat(entity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new NotificationEntity(null, userId, "Message", NotificationType.WARNING, "REF-123", true, null, null));
    }
}