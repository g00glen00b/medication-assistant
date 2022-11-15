package be.g00glen00b.apps.mediminder.availability.implementation;

import be.g00glen00b.apps.mediminder.notification.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.function.Function;


@RequiredArgsConstructor
public class MedicationAvailabilityNotificationWriter<T> implements ItemWriter<T> {
    private final Function<T, NotificationDTO> notificationFactory;

    @Override
    public void write(Chunk<? extends T> list) {
        list.forEach(notificationFactory::apply);
    }
}
