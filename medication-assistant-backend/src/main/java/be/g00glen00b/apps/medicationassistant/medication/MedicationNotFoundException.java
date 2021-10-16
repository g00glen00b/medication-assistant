package be.g00glen00b.apps.medicationassistant.medication;

class MedicationNotFoundException extends RuntimeException {
    public MedicationNotFoundException(String message) {
        super(message);
    }
}
