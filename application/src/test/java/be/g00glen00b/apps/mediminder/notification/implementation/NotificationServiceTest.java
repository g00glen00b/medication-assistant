package be.g00glen00b.apps.mediminder.notification.implementation;

import be.g00glen00b.apps.mediminder.notification.CreateOrUpdateNotificationRequestDTO;
import be.g00glen00b.apps.mediminder.notification.NotificationDTO;
import be.g00glen00b.apps.mediminder.notification.NotificationNotFoundException;
import be.g00glen00b.apps.mediminder.notification.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    private NotificationService service;
    @Mock
    private NotificationEntityRepository repository;
    @Captor
    private ArgumentCaptor<NotificationEntity> anyEntity;

    @BeforeEach
    void setUp() {
        service = new NotificationService(repository);
    }

    @Test
    void findAll_returnsResult() {
        UUID userId = UUID.randomUUID();
        NotificationEntity entity = NotificationEntity.of(userId, "REF-123", "Message", NotificationType.INFO);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(repository.findAllActiveByUserId(any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));

        Page<NotificationDTO> result = service.findAll(userId, pageRequest);
        assertThat(result.getPageable()).isEqualTo(pageRequest);
        assertThat(result.getContent()).contains(NotificationDTO.ofEntity(entity));
    }

    @Test
    void findAll_usesRepository() {
        UUID userId = UUID.randomUUID();
        NotificationEntity entity = NotificationEntity.of(userId, "REF-123", "Message", NotificationType.INFO);
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(repository.findAllActiveByUserId(any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));

        service.findAll(userId, pageRequest);
        verify(repository).findAllActiveByUserId(userId, pageRequest);
    }

    @Test
    void delete_updatesActiveState() {
        UUID userId = UUID.randomUUID();
        NotificationEntity entity = NotificationEntity.of(userId, "REF-123", "Message", NotificationType.INFO);
        when(repository.findByUserIdAndId(userId, entity.getId())).thenReturn(Optional.of(entity));

        service.delete(userId, entity.getId());
        assertThat(entity.isActive()).isFalse();
    }

    @Test
    void delete_usesRepository() {
        UUID userId = UUID.randomUUID();
        NotificationEntity entity = NotificationEntity.of(userId, "REF-123", "Message", NotificationType.INFO);
        when(repository.findByUserIdAndId(any(), any())).thenReturn(Optional.of(entity));

        service.delete(userId, entity.getId());
        verify(repository).findByUserIdAndId(userId, entity.getId());
    }

    @Test
    void delete_throwsExceptionIfEntityNotFound() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        assertThatExceptionOfType(NotificationNotFoundException.class)
            .isThrownBy(() -> service.delete(userId, id))
            .withMessage("Notification with ID '" + id + "' was not found");
    }

    @Test
    void findOrCreate_returnsResult() {
        UUID userId = UUID.randomUUID();
        NotificationEntity entity = NotificationEntity.of(userId, "REF-123", "Message", NotificationType.INFO);
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(NotificationType.WARNING, "REF-123", "Message 2");
        when(repository.findByUserIdAndReference(any(), any())).thenReturn(Optional.of(entity));

        NotificationDTO result = service.createOrUpdate(userId, request);
        assertThat(result).isEqualTo(new NotificationDTO(
            entity.getId(),
            request.message(),
            request.type(),
            null
        ));
    }

    @Test
    void findOrCreate_updatesExistingEntity() {
        UUID userId = UUID.randomUUID();
        NotificationEntity entity = NotificationEntity.of(userId, "REF-123", "Message", NotificationType.INFO);
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(NotificationType.WARNING, "REF-123", "Message 2");
        when(repository.findByUserIdAndReference(any(), any())).thenReturn(Optional.of(entity));

        service.createOrUpdate(userId, request);
        assertThat(entity.getMessage()).isEqualTo(request.message());
        assertThat(entity.getType()).isEqualTo(request.type());
    }

    @Test
    void findOrCreate_usesRepository() {
        UUID userId = UUID.randomUUID();
        NotificationEntity entity = NotificationEntity.of(userId, "REF-123", "Message", NotificationType.INFO);
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(NotificationType.WARNING, "REF-123", "Message 2");
        when(repository.findByUserIdAndReference(any(), any())).thenReturn(Optional.of(entity));

        service.createOrUpdate(userId, request);
        verify(repository).findByUserIdAndReference(userId, "REF-123");
    }

    @Test
    void findOrCreate_createsNewNotificationIfNotExisting() {
        UUID userId = UUID.randomUUID();
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(NotificationType.WARNING, "REF-123", "Message 2");
        when(repository.save(any())).thenAnswer(returnsFirstArg());

        NotificationDTO result = service.createOrUpdate(userId, request);
        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new NotificationDTO(null, "Message 2", NotificationType.WARNING, null));
        assertThat(result.id()).isNotNull();
    }

    @Test
    void findOrCreate_usesRepositoryToCreateNewNotification() {
        UUID userId = UUID.randomUUID();
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(NotificationType.WARNING, "REF-123", "Message 2");
        when(repository.save(any())).thenAnswer(returnsFirstArg());

        service.createOrUpdate(userId, request);
        verify(repository).save(anyEntity.capture());
        assertThat(anyEntity.getValue())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(NotificationEntity.of(userId, "REF-123", "Message 2", NotificationType.WARNING));
        assertThat(anyEntity.getValue().getId()).isNotNull();
    }
}