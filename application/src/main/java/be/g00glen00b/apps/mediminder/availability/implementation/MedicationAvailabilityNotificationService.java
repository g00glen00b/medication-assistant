package be.g00glen00b.apps.mediminder.availability.implementation;

import be.g00glen00b.apps.mediminder.medication.MedicationFacade;
import be.g00glen00b.apps.mediminder.notification.CreateOrUpdateNotificationRequestDTO;
import be.g00glen00b.apps.mediminder.notification.NotificationDTO;
import be.g00glen00b.apps.mediminder.notification.NotificationFacade;
import be.g00glen00b.apps.mediminder.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class MedicationAvailabilityNotificationService {
    private final NotificationFacade notificationFacade;
    private final MedicationFacade medicationFacade;

    public NotificationDTO createExpired(MedicationAvailabilityEntity entity) {
        String reference = "AVAILABILITY-EXPIRED-" + entity.getId();
        return notificationFacade.createOrUpdate(entity.getUserId(), new CreateOrUpdateNotificationRequestDTO(
            NotificationType.ERROR,
            reference,
            "Medication " + getMedicationName(entity) + " with expiry date " + getExpiryDate(entity) + " is expiring today"
        ));
    }

    public NotificationDTO createAlmostExpired(MedicationAvailabilityEntity entity) {
        String reference = "AVAILABILITY-ALMOST-EXPIRED-" + entity.getId();
        return notificationFacade.createOrUpdate(entity.getUserId(), new CreateOrUpdateNotificationRequestDTO(
            NotificationType.WARNING,
            reference,
            "Medication " + getMedicationName(entity) + " with expiry date " + getExpiryDate(entity) + " is expiring soon"
        ));
    }

    public NotificationDTO createAlmostNoQuantity(LowMedicationAvailabilityInfo info) {
        long epoch = info.getCreatedDate().toEpochMilli();
        String reference = "AVAILABILITY-ALMOST-NO-QUANTITY-" + info.getMedicationId() + "-" + epoch;
        return notificationFacade.createOrUpdate(info.getUserId(), new CreateOrUpdateNotificationRequestDTO(
            NotificationType.WARNING,
            reference,
            "You're almost running out of " + getMedicationName(info.getMedicationId())
        ));
    }

    public NotificationDTO createNoQuantity(LowMedicationAvailabilityInfo info) {
        long epoch = info.getCreatedDate().toEpochMilli();
        String reference = "AVAILABILITY-NO-QUANTITY-" + info.getMedicationId() + "-" + epoch;
        return notificationFacade.createOrUpdate(info.getUserId(), new CreateOrUpdateNotificationRequestDTO(
           NotificationType.ERROR,
           reference,
           "You have no " + getMedicationName(info.getMedicationId()) + " left"
        ));
    }

    private String getMedicationName(MedicationAvailabilityEntity entity) {
         return getMedicationName(entity.getMedicationId());
    }

    private String getMedicationName(UUID medicationId) {
        return medicationFacade.findByIdOrDummy(medicationId).name();
    }

    private String getExpiryDate(MedicationAvailabilityEntity entity) {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(entity.getExpiryDate());
    }
}
