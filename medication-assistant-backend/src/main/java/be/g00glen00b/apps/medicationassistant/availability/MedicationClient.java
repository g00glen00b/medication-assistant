package be.g00glen00b.apps.medicationassistant.availability;

import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationFacade;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MedicationClient {
    private final MedicationFacade medicationFacade;
    private final MedicationQuantityTypeFacade medicationQuantityTypeFacade;

    public Optional<MedicationDTO> findMedicationById(UUID medicationId) {
        return medicationFacade.findById(medicationId);
    }

    public Optional<MedicationQuantityTypeDTO> findQuantityTypeById(UUID quantityTypeId) {
        return medicationQuantityTypeFacade.findById(quantityTypeId);
    }
}
