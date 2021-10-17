package be.g00glen00b.apps.medicationassistant.availability;

import be.g00glen00b.apps.medicationassistant.schedule.MedicationScheduleCompletedEventDTO;
import be.g00glen00b.apps.medicationassistant.schedule.MedicationScheduleEventCompleteListener;
import be.g00glen00b.apps.medicationassistant.schedule.MedicationScheduleEventDTO;
import org.springframework.stereotype.Component;

@Component
class ReduceMedicationAvailabilityOnCompletedEventListener implements MedicationScheduleEventCompleteListener {
    @Override
    public void listen(MedicationScheduleCompletedEventDTO completedEvent) {

    }
}
