package be.g00glen00b.apps.mediminder.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.util.UUID;

public interface NotificationFacade {
    Page<NotificationDTO> findAll(UUID userId, Pageable pageable);

    @Transactional
    void delete(UUID userId, UUID id);

    @Transactional
    NotificationDTO createOrUpdate(UUID userId, @Valid CreateOrUpdateNotificationRequestDTO request);
}
