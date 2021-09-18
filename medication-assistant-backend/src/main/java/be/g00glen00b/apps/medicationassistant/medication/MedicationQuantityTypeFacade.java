package be.g00glen00b.apps.medicationassistant.medication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MedicationQuantityTypeFacade {
    Page<MedicationQuantityTypeDTO> findAll(Pageable pageable);
    Optional<MedicationQuantityTypeDTO> findById(UUID id);
    MedicationQuantityTypeDTO findOrCreate(MedicationQuantityTypeInputDTO input);
    MedicationQuantityTypeDTO create(MedicationQuantityTypeInputDTO input);
}
