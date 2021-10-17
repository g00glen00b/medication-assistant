package be.g00glen00b.apps.medicationassistant.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class NotificationService implements NotificationFacade {
    private final NotificationRepository repository;

    @Override
    public Page<NotificationDTO> findAll(UUID userId, Pageable pageable) {
        return repository
            .findAllByUserId(userId, pageable)
            .map(NotificationDTO::new);
    }

    @Override
    @Transactional
    public Optional<NotificationDTO> create(CreateNotificationRequestDTO request) {
        if (!request.isUniqueNotification() || !exists(request)) {
            Notification notification = new Notification(request.getUserId(), request.getRelevantId(), request.getType(), request.getMessage());
            return Optional.of(new NotificationDTO(repository.save(notification)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void delete(UUID id, UUID userId) {
        Notification notification = findByIdOrThrowException(id, userId);
        repository.delete(notification);
    }

    public boolean exists(CreateNotificationRequestDTO request) {
        return repository.existsByUserIdAndRelevantIdAndType(request.getUserId(), request.getRelevantId(), request.getType());
    }

    public Notification findByIdOrThrowException(UUID id, UUID userId) {
        return repository
            .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new NotificationNotFoundException("Notification with ID '" + id + "' does not exist"));
    }
}
