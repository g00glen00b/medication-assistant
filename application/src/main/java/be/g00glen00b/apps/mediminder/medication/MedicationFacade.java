package be.g00glen00b.apps.mediminder.medication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.UUID;

public interface MedicationFacade {
    Page<MedicationDTO> findAll(String search, Pageable pageable);

    MedicationDTO findById(UUID id);

    MedicationDTO findByIdOrDummy(UUID id);

    MedicationDTO findOrCreate(@Valid CreateMedicationRequestDTO request);

    Page<MedicationQuantityTypeDTO> findAllQuantityTypes(Pageable pageable);

    boolean existsById(UUID id);
}
