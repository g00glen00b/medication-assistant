package be.g00glen00b.apps.medicationassistant.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findAllByUserId(UUID userId, Pageable pageable);
    boolean existsByUserIdAndRelevantIdAndType(UUID userId, UUID availabilityId, NotificationType type);
    Optional<Notification> findByIdAndUserId(UUID id, UUID userId);
}
