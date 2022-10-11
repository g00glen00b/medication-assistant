package be.g00glen00b.apps.mediminder.notification.implementation;

import be.g00glen00b.apps.mediminder.notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {
    @Id
    private UUID id;
    private UUID userId;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String reference;
    private boolean active;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant lastModifiedDate;

    public static NotificationEntity of(UUID userId, String reference, String message, NotificationType type) {
        return new NotificationEntity(UUID.randomUUID(), userId, message, type, reference, true, null, null);
    }
}
