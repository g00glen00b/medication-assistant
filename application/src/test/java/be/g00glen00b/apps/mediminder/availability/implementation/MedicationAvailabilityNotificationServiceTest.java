package be.g00glen00b.apps.mediminder.availability.implementation;

import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationFacade;
import be.g00glen00b.apps.mediminder.medication.MedicationQuantityTypeDTO;
import be.g00glen00b.apps.mediminder.notification.CreateOrUpdateNotificationRequestDTO;
import be.g00glen00b.apps.mediminder.notification.NotificationDTO;
import be.g00glen00b.apps.mediminder.notification.NotificationFacade;
import be.g00glen00b.apps.mediminder.notification.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicationAvailabilityNotificationServiceTest {
    private MedicationAvailabilityNotificationService service;
    @Mock
    private NotificationFacade notificationFacade;
    @Mock
    private MedicationFacade medicationFacade;
    @Captor
    private ArgumentCaptor<CreateOrUpdateNotificationRequestDTO> anyNotificationRequest;

    @BeforeEach
    void setUp() {
        service = new MedicationAvailabilityNotificationService(notificationFacade, medicationFacade);
    }

    @Test
    void createExpired_createsExpiredNotification() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "Expired", NotificationType.ERROR, Instant.now());
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal initialQuantity = BigDecimal.ZERO;
        LocalDate expiryDate = LocalDate.of(2022, 10, 5);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);

        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        service.createExpired(entity);

        verify(notificationFacade).createOrUpdate(eq(userId), anyNotificationRequest.capture());
        assertThat(anyNotificationRequest.getValue()).isEqualTo(new CreateOrUpdateNotificationRequestDTO(
           NotificationType.ERROR,
           "AVAILABILITY-EXPIRED-" + entity.getId(),
           "Medication Hydrocortisone with expiry date 2022-10-05 is expiring today"
        ));
    }

    @Test
    void createExpired_returnsTheNotification() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "Expired", NotificationType.ERROR, Instant.now());
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal initialQuantity = BigDecimal.ZERO;
        LocalDate expiryDate = LocalDate.of(2022, 10, 5);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);

        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        NotificationDTO result = service.createExpired(entity);

        assertThat(result).isEqualTo(notification);
    }

    @Test
    void createExpired_retrievesTheMedicationName() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "Expired", NotificationType.ERROR, Instant.now());
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal initialQuantity = BigDecimal.ZERO;
        LocalDate expiryDate = LocalDate.of(2022, 10, 5);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);

        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        service.createExpired(entity);

        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void createAlmostExpired_createsExpiredNotification() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "Expired", NotificationType.ERROR, Instant.now());
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal initialQuantity = BigDecimal.ZERO;
        LocalDate expiryDate = LocalDate.of(2022, 10, 5);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);

        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        service.createAlmostExpired(entity);

        verify(notificationFacade).createOrUpdate(eq(userId), anyNotificationRequest.capture());
        assertThat(anyNotificationRequest.getValue()).isEqualTo(new CreateOrUpdateNotificationRequestDTO(
            NotificationType.WARNING,
            "AVAILABILITY-ALMOST-EXPIRED-" + entity.getId(),
            "Medication Hydrocortisone with expiry date 2022-10-05 is expiring soon"
        ));
    }

    @Test
    void createAlmostExpired_returnsTheNotification() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "Expired", NotificationType.ERROR, Instant.now());
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal initialQuantity = BigDecimal.ZERO;
        LocalDate expiryDate = LocalDate.of(2022, 10, 5);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);

        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        NotificationDTO result = service.createAlmostExpired(entity);

        assertThat(result).isEqualTo(notification);
    }

    @Test
    void createAlmostExpired_retrievesTheMedicationName() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "Expired", NotificationType.ERROR, Instant.now());
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal initialQuantity = BigDecimal.ZERO;
        LocalDate expiryDate = LocalDate.of(2022, 10, 5);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);

        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        service.createAlmostExpired(entity);

        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void createNoQuantity_createsNoQuantityNotification() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        ZonedDateTime createdDate = ZonedDateTime.of(2022, 10, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
        LowMedicationAvailabilityInfoMock info = new LowMedicationAvailabilityInfoMock(medicationId, userId, createdDate.toInstant());
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "No quantity", NotificationType.ERROR, Instant.now());
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);

        service.createNoQuantity(info);
        verify(notificationFacade).createOrUpdate(eq(userId), anyNotificationRequest.capture());
        assertThat(anyNotificationRequest.getValue()).isEqualTo(new CreateOrUpdateNotificationRequestDTO(
           NotificationType.ERROR,
           "AVAILABILITY-NO-QUANTITY-" + medicationId + "-" + createdDate.toInstant().toEpochMilli(),
           "You have no Hydrocortisone left"
        ));
    }

    @Test
    void createNoQuantity_returnsTheNotification() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        ZonedDateTime createdDate = ZonedDateTime.of(2022, 10, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
        LowMedicationAvailabilityInfoMock info = new LowMedicationAvailabilityInfoMock(medicationId, userId, createdDate.toInstant());
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "No quantity", NotificationType.ERROR, Instant.now());
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);

        NotificationDTO result = service.createNoQuantity(info);
        assertThat(result).isEqualTo(notification);
    }

    @Test
    void createNoQuantity_retrievesTheMedicationName() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        ZonedDateTime createdDate = ZonedDateTime.of(2022, 10, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
        LowMedicationAvailabilityInfoMock info = new LowMedicationAvailabilityInfoMock(medicationId, userId, createdDate.toInstant());
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "No quantity", NotificationType.ERROR, Instant.now());
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);

        service.createNoQuantity(info);
        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void createAlmostNoQuantity_createsNoQuantityNotification() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        ZonedDateTime createdDate = ZonedDateTime.of(2022, 10, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
        LowMedicationAvailabilityInfoMock info = new LowMedicationAvailabilityInfoMock(medicationId, userId, createdDate.toInstant());
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "No quantity", NotificationType.ERROR, Instant.now());
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);

        service.createAlmostNoQuantity(info);
        verify(notificationFacade).createOrUpdate(eq(userId), anyNotificationRequest.capture());
        assertThat(anyNotificationRequest.getValue()).isEqualTo(new CreateOrUpdateNotificationRequestDTO(
            NotificationType.WARNING,
            "AVAILABILITY-ALMOST-NO-QUANTITY-" + medicationId + "-" + createdDate.toInstant().toEpochMilli(),
            "You're almost running out of Hydrocortisone"
        ));
    }

    @Test
    void createAlmostNoQuantity_returnsTheNotification() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        ZonedDateTime createdDate = ZonedDateTime.of(2022, 10, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
        LowMedicationAvailabilityInfoMock info = new LowMedicationAvailabilityInfoMock(medicationId, userId, createdDate.toInstant());
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "No quantity", NotificationType.ERROR, Instant.now());
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);

        NotificationDTO result = service.createAlmostNoQuantity(info);
        assertThat(result).isEqualTo(notification);
    }

    @Test
    void createAlmostNoQuantity_retrievesTheMedicationName() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        ZonedDateTime createdDate = ZonedDateTime.of(2022, 10, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
        LowMedicationAvailabilityInfoMock info = new LowMedicationAvailabilityInfoMock(medicationId, userId, createdDate.toInstant());
        NotificationDTO notification = new NotificationDTO(UUID.randomUUID(), "No quantity", NotificationType.ERROR, Instant.now());
        when(notificationFacade.createOrUpdate(any(), any())).thenReturn(notification);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medication);

        service.createAlmostNoQuantity(info);
        verify(medicationFacade).findByIdOrDummy(medicationId);
    }
}