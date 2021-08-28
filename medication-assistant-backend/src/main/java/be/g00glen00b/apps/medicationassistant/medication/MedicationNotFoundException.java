package be.g00glen00b.apps.medicationassistant.medication;

public class MedicationNotFoundException extends RuntimeException {
    public MedicationNotFoundException(String message) {
        super(message);
    }
}
