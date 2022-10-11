package be.g00glen00b.apps.mediminder.schedule;

public class InvalidMedicationEventException extends RuntimeException {
    public InvalidMedicationEventException(String message) {
        super(message);
    }
}
