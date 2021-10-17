package be.g00glen00b.apps.medicationassistant.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface NotificationFacade {
    Page<NotificationDTO> findAll(UUID userId, Pageable pageable);
    Optional<NotificationDTO> create(CreateNotificationRequestDTO request);
    void delete(UUID id, UUID userId);
}
