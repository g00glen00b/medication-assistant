package be.g00glen00b.apps.medicationassistant.availability;

import be.g00glen00b.apps.medicationassistant.medication.*;
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

    public MedicationDTO findOrCreateMedication(String medicationName) {
        return medicationFacade.findOrCreate(new CreateMedicationRequestDTO(medicationName));
    }

    public Optional<MedicationQuantityTypeDTO> findQuantityTypeById(UUID quantityTypeId) {
        return medicationQuantityTypeFacade.findById(quantityTypeId);
    }
}
