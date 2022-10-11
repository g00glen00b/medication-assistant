package be.g00glen00b.apps.mediminder.notification.implementation;

import be.g00glen00b.apps.mediminder.notification.CreateOrUpdateNotificationRequestDTO;
import be.g00glen00b.apps.mediminder.notification.NotificationDTO;
import be.g00glen00b.apps.mediminder.notification.NotificationFacade;
import be.g00glen00b.apps.mediminder.notification.NotificationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
class NotificationService implements NotificationFacade {
    private final NotificationEntityRepository repository;

    @Override
    public Page<NotificationDTO> findAll(UUID userId, Pageable pageable) {
        return repository
            .findAllActiveByUserId(userId, pageable)
            .map(NotificationDTO::ofEntity);
    }

    @Override
    @Transactional
    public void delete(UUID userId, UUID id) {
        NotificationEntity entity = findByIdOrThrowException(userId, id);
        entity.setActive(false);
    }

    @Override
    @Transactional
    public NotificationDTO createOrUpdate(UUID userId, @Valid CreateOrUpdateNotificationRequestDTO request) {
        NotificationEntity entity = repository
            .findByUserIdAndReference(userId, request.reference())
            .orElseGet(() -> create(userId, request));
        entity.setType(request.type());
        entity.setMessage(request.message());
        return NotificationDTO.ofEntity(entity);
    }

    private NotificationEntity create(UUID userId, CreateOrUpdateNotificationRequestDTO request) {
        return repository.save(NotificationEntity.of(userId, request.reference(), request.message(), request.type()));
    }

    private NotificationEntity findByIdOrThrowException(UUID userId, UUID id) {
        return repository
            .findByUserIdAndId(userId, id)
            .orElseThrow(() -> new NotificationNotFoundException(id));
    }
}
