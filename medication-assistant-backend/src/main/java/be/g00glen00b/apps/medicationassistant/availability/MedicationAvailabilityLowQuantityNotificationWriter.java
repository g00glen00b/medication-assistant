package be.g00glen00b.apps.medicationassistant.availability;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
class MedicationAvailabilityLowQuantityNotificationWriter implements ItemWriter<MedicationAvailabilityPercentage> {
    private final MedicationClient medicationClient;
    private final NotificationClient notificationClient;

    @Override
    public void write(List<? extends MedicationAvailabilityPercentage> list) {
        list.forEach(percentage -> medicationClient
            .findMedicationById(percentage.getMedicationId())
            .ifPresent(medication -> notificationClient.createLowQuantity(percentage.getUserId(), medication)));
    }
}
