package be.g00glen00b.apps.mediminder.availability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface MedicationAvailabilityFacade {
    Page<MedicationAvailabilityDTO> findAllNonEmptyNonExpiredByUserId(UUID userId, Pageable pageable);

    List<MedicationAvailabilityTotalDTO> findAllTotalsByUserId(UUID userId);

    @Transactional
    MedicationAvailabilityDTO create(UUID userId, @Valid CreateMedicationAvailabilityRequestDTO request);

    @Transactional
    MedicationAvailabilityDTO update(UUID userId, UUID id, @Valid UpdateMedicationAvailabilityRequestDTO request);

    @Transactional
    void delete(UUID userId, UUID id);

    @Transactional
    MedicationAvailabilityDTO findById(UUID userId, UUID id);

    BigDecimal removeQuantity(UUID userId, UUID medicationId, BigDecimal quantity);
}
