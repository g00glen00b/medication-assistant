package be.g00glen00b.apps.mediminder.medication;

public class InvalidMedicationException extends RuntimeException {
    public InvalidMedicationException(String message) {
        super(message);
    }
}
