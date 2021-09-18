package be.g00glen00b.apps.medicationassistant.medication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicationQuantityTypeFacadeImpl implements MedicationQuantityTypeFacade {
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
    public MedicationQuantityTypeDTO findOrCreate(MedicationQuantityTypeInputDTO input) {
        return repository
            .findByNameIgnoreCase(input.getName())
            .map(MedicationQuantityTypeDTO::new)
            .orElseGet(() -> create(input));
    }

    @Override
    @Transactional
    public MedicationQuantityTypeDTO create(MedicationQuantityTypeInputDTO input) {
        log.debug("Creating quantity type: {}", input.getName());
        MedicationQuantityType result = repository.save(new MedicationQuantityType(input.getName()));
        return new MedicationQuantityTypeDTO(result);
    }
}
