package be.g00glen00b.apps.medicationassistant.availability;


import be.g00glen00b.apps.medicationassistant.notification.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
class MedicationAvailabilityNotificationWriter implements ItemWriter<MedicationAvailability> {
    private final MedicationAvailabilityService service;
    private final NotificationClient notificationClient;
    private final NotificationCreator notificationCreator;

    @Override
    public void write(List<? extends MedicationAvailability> list) {
        list.forEach(availability -> notificationCreator.apply(
            notificationClient,
            availability.getUserId(),
            service.mapToDTO(availability)
        ));
    }

    public interface NotificationCreator {
        Optional<NotificationDTO> apply(NotificationClient client, UUID userId, MedicationAvailabilityDTO availability);
    }
}
