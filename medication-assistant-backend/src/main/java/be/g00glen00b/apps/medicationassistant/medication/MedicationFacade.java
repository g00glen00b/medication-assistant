package be.g00glen00b.apps.medicationassistant.medication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

public interface MedicationFacade {
    Page<MedicationDTO> findAll(String search, Pageable pageable);
    Optional<MedicationDTO> findById(UUID id);
    MedicationDTO findOrCreate(@Valid CreateMedicationRequestDTO input);
}
