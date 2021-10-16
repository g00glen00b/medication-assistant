package be.g00glen00b.apps.medicationassistant.medication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
class MedicationQuantityTypeService implements MedicationQuantityTypeFacade {
    private final MedicationQuantityTypeRepository repository;

    @Override
    public Page<MedicationQuantityTypeDTO> findAll(Pageable pageable) {
        return repository
            .findAll(pageable)
            .map(MedicationQuantityTypeDTO::new);
    }

    @Override
    public Optional<MedicationQuantityTypeDTO> findById(UUID id) {
        return repository
            .findById(id)
            .map(MedicationQuantityTypeDTO::new);
    }

    @Override
    @Transactional
    public MedicationQuantityTypeDTO findOrCreate(@Valid CreateMedicationQuantityTypeRequestDTO input) {
        return repository
            .findByNameIgnoreCase(input.getName())
            .map(MedicationQuantityTypeDTO::new)
            .orElseGet(() -> create(input));
    }

    private MedicationQuantityTypeDTO create(CreateMedicationQuantityTypeRequestDTO input) {
        log.debug("Creating quantity type: {}", input.getName());
        MedicationQuantityType result = repository.save(new MedicationQuantityType(input.getName()));
        return new MedicationQuantityTypeDTO(result);
    }
}
