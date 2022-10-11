package be.g00glen00b.apps.mediminder.availability.implementation;

import be.g00glen00b.apps.mediminder.availability.MedicationAvailabilityFacade;
import be.g00glen00b.apps.mediminder.schedule.MedicationTakenEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class MedicationTakenEventListenerTest {
    private MedicationTakenEventListener listener;
    @Mock
    private MedicationAvailabilityFacade facade;

    @BeforeEach
    void setUp() {
        listener = new MedicationTakenEventListener(facade);
    }

    @Test
    void subtractFromAvailability_usesFacade() {
        UUID userId = UUID.randomUUID();
        UUID scheduleId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDateTime eventDate = LocalDateTime.of(2022, 10, 11, 8, 0, 0);
        LocalDateTime completedDate = LocalDateTime.of(2022, 10, 11, 8, 1, 0);
        listener.subtractFromAvailability(new MedicationTakenEvent(userId, scheduleId, medicationId, quantity, eventDate, completedDate));
    }
}