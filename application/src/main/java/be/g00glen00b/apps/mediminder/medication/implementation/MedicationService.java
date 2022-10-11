package be.g00glen00b.apps.mediminder.medication.implementation;

import be.g00glen00b.apps.mediminder.medication.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
class MedicationService implements MedicationFacade {
    private final MedicationEntityRepository repository;
    private final MedicationQuantityTypeEntityRepository quantityTypeRepository;

    @Override
    public Page<MedicationDTO> findAll(String search, Pageable pageable) {
        return repository
            .findAllByNameContainingIgnoreCase(search, pageable)
            .map(MedicationDTO::ofEntity);
    }

    @Override
    public MedicationDTO findById(UUID id) {
        return repository
            .findById(id)
            .map(MedicationDTO::ofEntity)
            .orElseThrow(() -> new MedicationNotFoundException(id));
    }

    @Override
    public MedicationDTO findByIdOrDummy(UUID id) {
        return repository.findById(id)
            .map(MedicationDTO::ofEntity)
            .orElse(MedicationDTO.ofId(id));
    }

    @Override
    @Transactional
    public MedicationDTO findOrCreate(@Valid CreateMedicationRequestDTO request) {
        MedicationEntity entity = repository
            .findByNameIgnoreCase(request.name())
            .orElseGet(() -> create(request));
        return MedicationDTO.ofEntity(entity);
    }

    @Override
    public Page<MedicationQuantityTypeDTO> findAllQuantityTypes(Pageable pageable) {
        return quantityTypeRepository
            .findAll(pageable)
            .map(MedicationQuantityTypeDTO::ofEntity);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    private MedicationEntity create(CreateMedicationRequestDTO request) {
        MedicationQuantityTypeEntity quantityType = findQuantityTypeById(request.quantityTypeId());
        MedicationEntity entity = MedicationEntity.of(request.name(), quantityType);
        return repository.save(entity);
    }

    private MedicationQuantityTypeEntity findQuantityTypeById(UUID quantityTypeId) {
        return quantityTypeRepository
            .findById(quantityTypeId)
            .orElseThrow(() -> new InvalidMedicationException("Quantity type with given ID does not exist"));
    }
}
