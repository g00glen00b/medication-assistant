package be.g00glen00b.apps.mediminder.schedule;

public class InvalidMedicationScheduleException extends RuntimeException {
    public InvalidMedicationScheduleException(String message) {
        super(message);
    }
}
