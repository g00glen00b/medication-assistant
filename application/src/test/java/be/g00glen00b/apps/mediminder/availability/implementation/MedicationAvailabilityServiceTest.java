package be.g00glen00b.apps.mediminder.availability.implementation;

import be.g00glen00b.apps.mediminder.availability.*;
import be.g00glen00b.apps.mediminder.medication.CreateMedicationRequestDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationFacade;
import be.g00glen00b.apps.mediminder.medication.MedicationQuantityTypeDTO;
import be.g00glen00b.apps.mediminder.user.UserFacade;
import be.g00glen00b.apps.mediminder.user.UserInfoDTO;
import be.g00glen00b.apps.mediminder.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationAvailabilityServiceTest {
    private static final ZonedDateTime TODAY = ZonedDateTime.of(2022, 10, 6, 18, 0, 0, 0, ZoneId.of("UTC"));
    private MedicationAvailabilityService service;
    @Mock
    private MedicationAvailabilityEntityRepository repository;
    @Mock
    private MedicationFacade medicationFacade;
    @Mock
    private UserFacade userFacade;
    @Captor
    private ArgumentCaptor<CreateMedicationRequestDTO> anyCreateMedicationRequest;
    @Captor
    private ArgumentCaptor<MedicationAvailabilityEntity> anyEntity;

    @BeforeEach
    void setUp() {
        service = new MedicationAvailabilityService(repository, medicationFacade, userFacade);
    }

    @Test
    void findAllNonEmptyNonExpiredByUserId_returnsResult() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Anakletos Fiachna", ZoneId.of("Europe/Brussels"));
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("2"), TODAY.plusDays(2).toLocalDate());
        Pageable pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityEntity> entityPage = new PageImpl<>(List.of(entity), pageRequest, 1);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserId(any(), any(), any())).thenReturn(entityPage);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        Page<MedicationAvailabilityDTO> result = service.findAllNonEmptyNonExpiredByUserId(userId, pageRequest);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getPageable()).isEqualTo(pageRequest);
        assertThat(result.getContent()).containsOnly(new MedicationAvailabilityDTO(
            entity.getId(),
            medicationDTO,
            entity.getQuantity(),
            entity.getInitialQuantity(),
            entity.getExpiryDate()
        ));
    }

    @Test
    void findAllNonEmptyNonExpiredByUserId_usesRepository() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Anakletos Fiachna", ZoneId.of("Europe/Brussels"));
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("2"), TODAY.plusDays(2).toLocalDate());
        Pageable pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityEntity> entityPage = new PageImpl<>(List.of(entity), pageRequest, 1);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserId(any(), any(), any())).thenReturn(entityPage);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findAllNonEmptyNonExpiredByUserId(userId, pageRequest);
        verify(repository).findAllNonEmptyNonExpiredByUserId(userId, TODAY.toLocalDate(), pageRequest);
    }

    @Test
    void findAllNonEmptyNonExpiredByUserId_retrievesMedicationInfo() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Anakletos Fiachna", ZoneId.of("Europe/Brussels"));
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("2"), TODAY.plusDays(2).toLocalDate());
        Pageable pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityEntity> entityPage = new PageImpl<>(List.of(entity), pageRequest, 1);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserId(any(), any(), any())).thenReturn(entityPage);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findAllNonEmptyNonExpiredByUserId(userId, pageRequest);
        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void findAllNonEmptyNonExpiredByUserId_retrievesUserInfoForTimezone() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Anakletos Fiachna", ZoneId.of("Europe/Brussels"));
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("2"), TODAY.plusDays(2).toLocalDate());
        Pageable pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityEntity> entityPage = new PageImpl<>(List.of(entity), pageRequest, 1);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserId(any(), any(), any())).thenReturn(entityPage);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findAllNonEmptyNonExpiredByUserId(userId, pageRequest);
        verify(userFacade).findById(userId);
    }

    @Test
    void findAllNonEmptyNonExpiredByUserId_mapsToLocalDateUser() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Anakletos Fiachna", ZoneId.of("Australia/Brisbane"));
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("2"), TODAY.plusDays(2).toLocalDate());
        Pageable pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityEntity> entityPage = new PageImpl<>(List.of(entity), pageRequest, 1);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserId(any(), any(), any())).thenReturn(entityPage);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findAllNonEmptyNonExpiredByUserId(userId, pageRequest);
        // Australia/Brisbane is 10 hours behind UTC time, which turns it into the next date if 18:00:00 UTC time
        verify(repository).findAllNonEmptyNonExpiredByUserId(userId, TODAY.toLocalDate().plusDays(1), pageRequest);
    }

    @Test
    void findAllNonEmptyNonExpiredByUserId_usesUTCAsDefaultTimezoneIfUserNotFound() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("2"), TODAY.plusDays(2).toLocalDate());
        Pageable pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityEntity> entityPage = new PageImpl<>(List.of(entity), pageRequest, 1);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(userFacade.findById(userId)).thenThrow(new UserNotFoundException(userId));
        when(repository.findAllNonEmptyNonExpiredByUserId(any(), any(), any())).thenReturn(entityPage);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findAllNonEmptyNonExpiredByUserId(userId, pageRequest);
        verify(repository).findAllNonEmptyNonExpiredByUserId(userId, TODAY.toLocalDate(), pageRequest);
    }

    @Test
    void create_returnsResult() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate expiresTomorrow = TODAY.plusDays(1).toLocalDate();
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO("Hydrocortisone", quantityTypeId, new BigDecimal("1"), new BigDecimal("10"), expiresTomorrow);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(repository.save(any())).thenAnswer(returnsFirstArg());
        when(userFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.findOrCreate(any())).thenReturn(medicationDTO);

        MedicationAvailabilityDTO result = service.create(userId, request);
        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new MedicationAvailabilityDTO(
                null,
                medicationDTO,
                request.quantity(),
                request.initialQuantity(),
                request.expiryDate()
            ));
        assertThat(result.id()).isNotNull();
    }

    @Test
    void create_validatesExistingUser() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate expiresTomorrow = TODAY.plusDays(1).toLocalDate();
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO("Hydrocortisone", quantityTypeId, new BigDecimal("1"), new BigDecimal("10"), expiresTomorrow);
        when(userFacade.existsById(any())).thenReturn(false);

        assertThatExceptionOfType(InvalidMedicationAvailabilityException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessage("User is not valid");
        verifyNoInteractions(repository, medicationFacade);
        verify(userFacade).existsById(userId);
    }

    @Test
    void create_validatesQuantity() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate expiresTomorrow = TODAY.plusDays(1).toLocalDate();
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO("Hydrocortisone", quantityTypeId, new BigDecimal("11"), new BigDecimal("10"), expiresTomorrow);
        when(userFacade.existsById(any())).thenReturn(true);

        assertThatExceptionOfType(InvalidMedicationAvailabilityException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessage("The quantity of the medication exceeds the initial quantity");
        verifyNoInteractions(repository, medicationFacade);
    }

    @Test
    void create_createsTheMedication() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate expiresTomorrow = TODAY.plusDays(1).toLocalDate();
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO("Hydrocortisone", quantityTypeId, new BigDecimal("1"), new BigDecimal("10"), expiresTomorrow);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(repository.save(any())).thenAnswer(returnsFirstArg());
        when(userFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.findOrCreate(any())).thenReturn(medicationDTO);

        service.create(userId, request);
        verify(medicationFacade).findOrCreate(anyCreateMedicationRequest.capture());
        assertThat(anyCreateMedicationRequest.getValue()).isEqualTo(new CreateMedicationRequestDTO(
            "Hydrocortisone",
            quantityTypeId
        ));
    }

    @Test
    void create_savesTheEntity() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate expiresTomorrow = TODAY.plusDays(1).toLocalDate();
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO("Hydrocortisone", quantityTypeId, new BigDecimal("1"), new BigDecimal("10"), expiresTomorrow);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(repository.save(any())).thenAnswer(returnsFirstArg());
        when(userFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.findOrCreate(any())).thenReturn(medicationDTO);

        service.create(userId, request);
        verify(repository).save(anyEntity.capture());
        assertThat(anyEntity.getValue())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(MedicationAvailabilityEntity.of(
                medicationId,
                userId,
                request.quantity(),
                request.initialQuantity(),
                request.expiryDate()
            ));
        assertThat(anyEntity.getValue().getId()).isNotNull();
    }

    @Test
    void update_returnsResult() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate oldExpiryDate = TODAY.plusDays(2).toLocalDate();
        LocalDate newExpiryDate = TODAY.plusDays(1).toLocalDate();
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(new BigDecimal("2"), new BigDecimal("20"), newExpiryDate);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("10"), oldExpiryDate);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(userFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        MedicationAvailabilityDTO result = service.update(userId, entity.getId(), request);
        assertThat(result)
            .isEqualTo(new MedicationAvailabilityDTO(
                entity.getId(),
                medicationDTO,
                request.quantity(),
                request.initialQuantity(),
                request.expiryDate()
            ));
    }

    @Test
    void update_setsInitialQuantityToQuantityIfNull() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate oldExpiryDate = TODAY.plusDays(2).toLocalDate();
        LocalDate newExpiryDate = TODAY.plusDays(1).toLocalDate();
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(new BigDecimal("2"), null, newExpiryDate);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("10"), oldExpiryDate);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(userFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        MedicationAvailabilityDTO result = service.update(userId, entity.getId(), request);
        assertThat(result)
            .isEqualTo(new MedicationAvailabilityDTO(
                entity.getId(),
                medicationDTO,
                request.quantity(),
                request.quantity(),
                request.expiryDate()
            ));
    }

    @Test
    void update_validatesUserId() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate oldExpiryDate = TODAY.plusDays(2).toLocalDate();
        LocalDate newExpiryDate = TODAY.plusDays(1).toLocalDate();
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(new BigDecimal("2"), new BigDecimal("20"), newExpiryDate);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("10"), oldExpiryDate);
        when(userFacade.existsById(any())).thenReturn(false);

        assertThatExceptionOfType(InvalidMedicationAvailabilityException.class)
            .isThrownBy(() -> service.update(userId, entity.getId(), request))
            .withMessage("User is not valid");
        verifyNoInteractions(medicationFacade, repository);
        verify(userFacade).existsById(userId);
        verifyNoInteractions(repository, medicationFacade);
    }

    @Test
    void update_validatesQuantity() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate oldExpiryDate = TODAY.plusDays(2).toLocalDate();
        LocalDate newExpiryDate = TODAY.plusDays(1).toLocalDate();
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(new BigDecimal("22"), new BigDecimal("20"), newExpiryDate);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("10"), oldExpiryDate);
        when(userFacade.existsById(any())).thenReturn(true);

        assertThatExceptionOfType(InvalidMedicationAvailabilityException.class)
            .isThrownBy(() -> service.update(userId, entity.getId(), request))
            .withMessage("The quantity of the medication exceeds the initial quantity");
        verifyNoInteractions(repository, medicationFacade);
    }

    @Test
    void update_updatesExistingEntity() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate oldExpiryDate = TODAY.plusDays(2).toLocalDate();
        LocalDate newExpiryDate = TODAY.plusDays(1).toLocalDate();
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(new BigDecimal("2"), new BigDecimal("20"), newExpiryDate);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("10"), oldExpiryDate);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(userFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.update(userId, entity.getId(), request);
        verify(repository).findByIdAndUserId(entity.getId(), userId);
        assertThat(entity)
            .usingRecursiveComparison()
            .isEqualTo(new MedicationAvailabilityEntity(
                entity.getId(),
                medicationId,
                userId,
                request.quantity(),
                request.initialQuantity(),
                request.expiryDate(),
                null,
                null
            ));
    }

    @Test
    void update_retrievesTheMedication() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate oldExpiryDate = TODAY.plusDays(2).toLocalDate();
        LocalDate newExpiryDate = TODAY.plusDays(1).toLocalDate();
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(new BigDecimal("2"), new BigDecimal("20"), newExpiryDate);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("10"), oldExpiryDate);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(userFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        MedicationAvailabilityDTO result = service.update(userId, entity.getId(), request);
        assertThat(result)
            .isEqualTo(new MedicationAvailabilityDTO(
                entity.getId(),
                medicationDTO,
                request.quantity(),
                request.initialQuantity(),
                request.expiryDate()
            ));
    }

    @Test
    void update_validatesIfEntityExists() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate oldExpiryDate = TODAY.plusDays(2).toLocalDate();
        LocalDate newExpiryDate = TODAY.plusDays(1).toLocalDate();
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(new BigDecimal("2"), new BigDecimal("20"), newExpiryDate);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("10"), oldExpiryDate);
        when(userFacade.existsById(any())).thenReturn(true);

        assertThatExceptionOfType(MedicationAvailabilityNotFoundException.class)
            .isThrownBy(() -> service.update(userId, entity.getId(), request))
            .withMessage("Medication availability with ID '" + entity.getId() + "' was not found");
        verifyNoInteractions(medicationFacade);
    }

    @Test
    void delete_deletesEntity() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate expiryDate = TODAY.plusDays(1).toLocalDate();
        BigDecimal quantity = new BigDecimal("1");
        BigDecimal initialQuantity = new BigDecimal("10");
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));

        service.delete(userId, entity.getId());
        verify(repository).findByIdAndUserId(entity.getId(), userId);
        verify(repository).delete(entity);
    }

    @Test
    void delete_throwsExceptionIfEntityNotFound() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        assertThatExceptionOfType(MedicationAvailabilityNotFoundException.class)
            .isThrownBy(() -> service.delete(userId, id))
            .withMessage("Medication availability with ID '" + id + "' was not found");
    }

    @Test
    void findById_returnsResult() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate expiryDate = TODAY.plusDays(2).toLocalDate();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("10"), expiryDate);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        MedicationAvailabilityDTO result = service.findById(userId, entity.getId());
        assertThat(result)
            .isEqualTo(new MedicationAvailabilityDTO(
                entity.getId(),
                medicationDTO,
                entity.getQuantity(),
                entity.getInitialQuantity(),
                entity.getExpiryDate()
            ));
    }

    @Test
    void findById_retrievesEntity() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate expiryDate = TODAY.plusDays(2).toLocalDate();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("10"), expiryDate);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findById(userId, entity.getId());
        verify(repository).findByIdAndUserId(entity.getId(), userId);
    }

    @Test
    void findById_throwsExceptionIfNotFound() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        assertThatExceptionOfType(MedicationAvailabilityNotFoundException.class)
            .isThrownBy(() -> service.findById(userId, id))
            .withMessage("Medication availability with ID '" + id + "' was not found");
        verifyNoInteractions(medicationFacade);
    }

    @Test
    void findById_retrievesMedication() {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        LocalDate expiryDate = TODAY.plusDays(2).toLocalDate();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("1"), new BigDecimal("10"), expiryDate);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findById(userId, entity.getId());
        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void removeQuantity_removesQuantityFromAvailabilities() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Faisal Victoria", ZoneId.of("Europe/Brussels"));
        LocalDate expiryDate = TODAY.toLocalDate().plusDays(1);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("3"), new BigDecimal("10"), expiryDate);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserIdAndMedicationId(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(entity)));

        service.removeQuantity(userId, medicationId, new BigDecimal("1"));
        assertThat(entity.getQuantity()).isEqualTo("2");
    }

    @Test
    void removeQuantity_returnsRemainderIfTooMuchToSubtract() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Faisal Victoria", ZoneId.of("Europe/Brussels"));
        LocalDate expiryDate = TODAY.toLocalDate().plusDays(1);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("3"), new BigDecimal("10"), expiryDate);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserIdAndMedicationId(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(entity)));

        BigDecimal result = service.removeQuantity(userId, medicationId, new BigDecimal("4"));
        assertThat(entity.getQuantity()).isEqualTo("0");
        assertThat(result).isEqualTo("1");
    }

    @Test
    void removeQuantity_removesQuantityFromMultipleEntitiesIfPossible() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Faisal Victoria", ZoneId.of("Europe/Brussels"));
        LocalDate expiryDate = TODAY.toLocalDate().plusDays(1);
        MedicationAvailabilityEntity entityThreeQuantity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("3"), new BigDecimal("10"), expiryDate);
        MedicationAvailabilityEntity entityTwoQuantity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("2"), new BigDecimal("10"), expiryDate);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserIdAndMedicationId(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(entityThreeQuantity, entityTwoQuantity)));

        BigDecimal result = service.removeQuantity(userId, medicationId, new BigDecimal("4"));
        assertThat(entityThreeQuantity.getQuantity()).isEqualTo("0");
        assertThat(entityTwoQuantity.getQuantity()).isEqualTo("1");
        assertThat(result).isEqualTo("0");
    }

    @Test
    void removeQuantity_returnsRemainderOfMultipleSubtractions() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Faisal Victoria", ZoneId.of("Europe/Brussels"));
        LocalDate expiryDate = TODAY.toLocalDate().plusDays(1);
        MedicationAvailabilityEntity entityThreeQuantity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("3"), new BigDecimal("10"), expiryDate);
        MedicationAvailabilityEntity entityTwoQuantity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("2"), new BigDecimal("10"), expiryDate);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserIdAndMedicationId(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(entityThreeQuantity, entityTwoQuantity)));

        BigDecimal result = service.removeQuantity(userId, medicationId, new BigDecimal("6"));
        assertThat(entityThreeQuantity.getQuantity()).isEqualTo("0");
        assertThat(entityTwoQuantity.getQuantity()).isEqualTo("0");
        assertThat(result).isEqualTo("1");
    }

    @Test
    void removeQuantity_retrievesAllUnexpiredEntitiesWithQuantity() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Faisal Victoria", ZoneId.of("Europe/Brussels"));
        LocalDate expiryDate = TODAY.toLocalDate().plusDays(1);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("3"), new BigDecimal("10"), expiryDate);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserIdAndMedicationId(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(entity)));

        service.removeQuantity(userId, medicationId, new BigDecimal("4"));
        verify(repository).findAllNonEmptyNonExpiredByUserIdAndMedicationId(userId, medicationId, TODAY.toLocalDate(), PageRequest.of(0, 100, Sort.by("expiryDate")));
    }

    @Test
    void removeQuantity_usesTimezoneOfUserToCheckExpiryDate() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(userId, "me@example.org", "Faisal Victoria", ZoneId.of("Australia/Brisbane"));
        LocalDate expiryDate = TODAY.toLocalDate().plusDays(1);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, new BigDecimal("3"), new BigDecimal("10"), expiryDate);
        when(userFacade.findById(userId)).thenReturn(user);
        when(repository.findAllNonEmptyNonExpiredByUserIdAndMedicationId(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(entity)));

        service.removeQuantity(userId, medicationId, new BigDecimal("4"));
        verify(repository).findAllNonEmptyNonExpiredByUserIdAndMedicationId(userId, medicationId, TODAY.toLocalDate().plusDays(1), PageRequest.of(0, 100, Sort.by("expiryDate")));
    }
}