package be.g00glen00b.apps.medicationassistant.availability;

import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.notification.CreateNotificationRequestDTO;
import be.g00glen00b.apps.medicationassistant.notification.NotificationDTO;
import be.g00glen00b.apps.medicationassistant.notification.NotificationFacade;
import be.g00glen00b.apps.medicationassistant.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationClient {
    private final NotificationFacade facade;

    public Optional<NotificationDTO> createExpired(UUID userId, MedicationAvailabilityDTO availability) {
        return facade.create(new CreateNotificationRequestDTO(
            userId,
            availability.getId(),
            NotificationType.MEDICATION_EXPIRED,
            "Medication '" + availability.getMedication().getName() + "' with expiry date '" + availability.getExpiryDate().toString() + "' is expired"
        ));
    }

    public Optional<NotificationDTO> createSoonExpired(UUID userId, MedicationAvailabilityDTO availability) {
        return facade.create(new CreateNotificationRequestDTO(
            userId,
            availability.getId(),
            NotificationType.MEDICATION_SOON_EXPIRING,
            "Medication '" + availability.getMedication().getName() + "' with expiry date '" + availability.getExpiryDate().toString() + "' will soon expire"
        ));
    }

    public Optional<NotificationDTO> createUnavailable(UUID userId, MedicationDTO medication) {
        return facade.create(new CreateNotificationRequestDTO(
            userId,
            medication.getId(),
            NotificationType.MEDICATION_UNAVAILABLE,
            "Medication '" + medication.getName() + "' is not available, visit the pharmacy"
        ));
    }

    public Optional<NotificationDTO> createLowQuantity(UUID userId, MedicationDTO medication) {
        return facade.create(new CreateNotificationRequestDTO(
            userId,
            medication.getId(),
            NotificationType.MEDICATION_LOW_QUANTITY,
            "Medication '" + medication.getName() + "' has a low quantity"
        ));
    }
}
