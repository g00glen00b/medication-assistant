package be.g00glen00b.apps.medicationassistant.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
class Notification {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Type(type = "pg-uuid")
    private UUID id;
    private UUID userId;
    private UUID relevantId;
    private NotificationType type;
    private String message;

    public Notification(UUID userId, UUID relevantId, NotificationType type, String message) {
        this(null, userId, relevantId, type, message);
    }
}
