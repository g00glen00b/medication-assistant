package be.g00glen00b.apps.mediminder.availability.implementation;

import be.g00glen00b.apps.mediminder.notification.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.function.Function;


@RequiredArgsConstructor
public class MedicationAvailabilityNotificationWriter<T> implements ItemWriter<T> {
    private final Function<T, NotificationDTO> notificationFactory;

    @Override
    public void write(List<? extends T> list) {
        list.forEach(notificationFactory::apply);
    }
}
