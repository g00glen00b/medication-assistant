package be.g00glen00b.apps.medicationassistant.schedule;

public interface MedicationScheduleEventCompleteListener {
    void listen(MedicationScheduleCompletedEventDTO completedEvent);
}
