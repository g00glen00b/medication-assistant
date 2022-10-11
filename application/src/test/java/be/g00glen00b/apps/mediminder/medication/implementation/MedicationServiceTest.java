package be.g00glen00b.apps.mediminder.medication.implementation;

import be.g00glen00b.apps.mediminder.medication.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {
    private MedicationService service;
    @Mock
    private MedicationEntityRepository repository;
    @Mock
    private MedicationQuantityTypeEntityRepository quantityTypeRepository;
    @Captor
    private ArgumentCaptor<MedicationEntity> anyEntity;

    @BeforeEach
    void setUp() {
        service = new MedicationService(repository, quantityTypeRepository);
    }

    @Test
    void findAll_returnsResult() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        MedicationEntity entity = MedicationEntity.of("Hydrocortisone", quantityType);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        when(repository.findAllByNameContainingIgnoreCase(any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));

        Page<MedicationDTO> result = service.findAll("cort", pageRequest);
        assertThat(result.getPageable()).isEqualTo(pageRequest);
        assertThat(result.getContent()).contains(MedicationDTO.ofEntity(entity));
    }

    @Test
    void findAll_usesRepository() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        MedicationEntity entity = MedicationEntity.of("Hydrocortisone", quantityType);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        when(repository.findAllByNameContainingIgnoreCase(any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));

        service.findAll("cort", pageRequest);
        verify(repository).findAllByNameContainingIgnoreCase("cort", pageRequest);
    }

    @Test
    void findById_returnsResult() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        MedicationEntity entity = MedicationEntity.of("Hydrocortisone", quantityType);
        when(repository.findById(any())).thenReturn(Optional.of(entity));

        MedicationDTO result = service.findById(entity.getId());
        assertThat(result).isEqualTo(MedicationDTO.ofEntity(entity));
    }

    @Test
    void findById_usesRepository() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        MedicationEntity entity = MedicationEntity.of("Hydrocortisone", quantityType);
        when(repository.findById(any())).thenReturn(Optional.of(entity));

        service.findById(entity.getId());
        verify(repository).findById(entity.getId());
    }

    @Test
    void findById_throwsExceptionIfNoEntityFound() {
        UUID id = UUID.randomUUID();
        assertThatExceptionOfType(MedicationNotFoundException.class)
            .isThrownBy(() -> service.findById(id))
            .withMessage("Medication with ID '" + id + "' was not found");
    }

    @Test
    void findByIdOrDummy_returnsResult() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        MedicationEntity entity = MedicationEntity.of("Hydrocortisone", quantityType);
        when(repository.findById(any())).thenReturn(Optional.of(entity));

        MedicationDTO result = service.findByIdOrDummy(entity.getId());
        assertThat(result).isEqualTo(MedicationDTO.ofEntity(entity));
    }

    @Test
    void findByIdOrDummy_usesRepository() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        MedicationEntity entity = MedicationEntity.of("Hydrocortisone", quantityType);

        service.findByIdOrDummy(entity.getId());
        verify(repository).findById(entity.getId());
    }

    @Test
    void findByIdOrDummy_returnsNothingIfNoEntityFound() {
        UUID id = UUID.randomUUID();

        MedicationDTO result = service.findByIdOrDummy(id);
        assertThat(result).isEqualTo(MedicationDTO.ofId(id));
    }

    @Test
    void findOrCreate_returnsExistingEntity() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        MedicationEntity entity = MedicationEntity.of("Hydrocortisone", quantityType);
        CreateMedicationRequestDTO request = new CreateMedicationRequestDTO("Hydrocortisone", quantityType.getId());
        when(repository.findByNameIgnoreCase(any())).thenReturn(Optional.of(entity));

        MedicationDTO result = service.findOrCreate(request);
        assertThat(result).isEqualTo(MedicationDTO.ofEntity(entity));
    }

    @Test
    void findOrCreate_usesRepository() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        MedicationEntity entity = MedicationEntity.of("Hydrocortisone", quantityType);
        CreateMedicationRequestDTO request = new CreateMedicationRequestDTO("Hydrocortisone", quantityType.getId());
        when(repository.findByNameIgnoreCase(any())).thenReturn(Optional.of(entity));

        service.findOrCreate(request);
        verify(repository).findByNameIgnoreCase("Hydrocortisone");
    }

    @Test
    void findOrCreate_createsEntityIfNotFound() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        CreateMedicationRequestDTO request = new CreateMedicationRequestDTO("Hydrocortisone", quantityType.getId());
        when(quantityTypeRepository.findById(quantityType.getId())).thenReturn(Optional.of(quantityType));
        when(repository.save(any())).thenAnswer(returnsFirstArg());

        MedicationDTO result = service.findOrCreate(request);
        verify(repository).save(anyEntity.capture());
        assertThat(anyEntity.getValue())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new MedicationEntity(null, "Hydrocortisone", quantityType, null, null));
        assertThat(result)
            .isEqualTo(MedicationDTO.ofEntity(anyEntity.getValue()));
    }

    @Test
    void findOrCreate_throwsExceptionIfCreatingWithInvalidQuantityTypeId() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        CreateMedicationRequestDTO request = new CreateMedicationRequestDTO("Hydrocortisone", quantityType.getId());

        assertThatExceptionOfType(InvalidMedicationException.class)
            .isThrownBy(() -> service.findOrCreate(request))
            .withMessage("Quantity type with given ID does not exist");
        verify(repository).findByNameIgnoreCase("Hydrocortisone");
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAllQuantityTypes_returnsResults() {
        MedicationQuantityTypeEntity entity = MedicationQuantityTypeEntity.of("ml");
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(quantityTypeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));

        Page<MedicationQuantityTypeDTO> result = service.findAllQuantityTypes(pageRequest);
        assertThat(result.getPageable()).isEqualTo(pageRequest);
        assertThat(result.getContent()).contains(MedicationQuantityTypeDTO.ofEntity(entity));
    }

    @Test
    void findAllQuantityTypes_usesRepository() {
        MedicationQuantityTypeEntity entity = MedicationQuantityTypeEntity.of("ml");
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(quantityTypeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));

        service.findAllQuantityTypes(pageRequest);
        verify(quantityTypeRepository).findAll(pageRequest);
    }

    @Test
    void existsById_returnsResult() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(any())).thenReturn(true);
        assertThat(service.existsById(id)).isTrue();
        verify(repository).existsById(id);
    }
}