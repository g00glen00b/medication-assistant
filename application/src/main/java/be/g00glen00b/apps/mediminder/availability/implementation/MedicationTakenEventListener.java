package be.g00glen00b.apps.mediminder.availability.implementation;

import be.g00glen00b.apps.mediminder.availability.MedicationAvailabilityFacade;
import be.g00glen00b.apps.mediminder.schedule.MedicationTakenEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MedicationTakenEventListener {
    private final MedicationAvailabilityFacade facade;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void subtractFromAvailability(MedicationTakenEvent event) {
        facade.removeQuantity(event.userId(), event.medicationId(), event.quantity());
    }
}
