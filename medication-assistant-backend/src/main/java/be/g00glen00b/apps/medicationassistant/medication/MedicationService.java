package be.g00glen00b.apps.medicationassistant.medication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository repository;

    public Page<MedicationDTO> findAll(String search, Pageable pageable) {
        String wildcardSearch = "%" + search + "%";
        return repository
            .findAllByNameLike(wildcardSearch, pageable)
            .map(MedicationDTO::new);
    }

    public Optional<MedicationDTO> findById(UUID id) {
        return repository
            .findById(id)
            .map(MedicationDTO::new);
    }

    @Transactional
    public MedicationDTO findOrCreate(MedicationInputDTO input) {
        return repository
            .findByNameIgnoreCase(input.getName())
            .map(MedicationDTO::new)
            .orElseGet(() -> create(input));
    }

    @Transactional
    public MedicationDTO create(MedicationInputDTO input) {
        Medication result = repository.save(new Medication(input.getName()));
        return new MedicationDTO(result);
    }
}
