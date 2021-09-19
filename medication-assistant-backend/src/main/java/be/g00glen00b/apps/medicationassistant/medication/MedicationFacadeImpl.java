package be.g00glen00b.apps.medicationassistant.medication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class MedicationFacadeImpl implements MedicationFacade {
    private final MedicationRepository repository;

    @Override
    public Page<MedicationDTO> findAll(String search, Pageable pageable) {
        String wildcardSearch = "%" + search + "%";
        return repository
            .findAllByNameLike(wildcardSearch, pageable)
            .map(MedicationDTO::new);
    }

    @Override
    public Optional<MedicationDTO> findById(UUID id) {
        return repository
            .findById(id)
            .map(MedicationDTO::new);
    }

    @Override
    @Transactional
    public MedicationDTO findOrCreate(@Valid CreateMedicationRequestDTO input) {
        return repository
            .findByNameIgnoreCase(input.getName())
            .map(MedicationDTO::new)
            .orElseGet(() -> create(input));
    }

    private MedicationDTO create(CreateMedicationRequestDTO input) {
        Medication result = repository.save(new Medication(input.getName()));
        return new MedicationDTO(result);
    }
}
