package be.g00glen00b.apps.mediminder.schedule.implementation;

import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationFacade;
import be.g00glen00b.apps.mediminder.medication.MedicationQuantityTypeDTO;
import be.g00glen00b.apps.mediminder.schedule.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationScheduleServiceTest {
    private static final ZonedDateTime TODAY = ZonedDateTime.of(2022, 10, 7, 10, 0, 0, 0, ZoneId.of("UTC"));
    private MedicationScheduleService service;
    @Mock
    private MedicationScheduleEntityRepository repository;
    @Mock
    private MedicationFacade medicationFacade;
    @Mock
    private UserFacade userFacade;
    @Captor
    private ArgumentCaptor<MedicationScheduleEntity> anyEntity;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(TODAY.toInstant(), TODAY.getZone());
        service = new MedicationScheduleService(repository, medicationFacade, userFacade, fixedClock);
    }

    @Test
    void findAll_returnsResults() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(TODAY.toLocalDate());
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before breakfast");
        PageRequest pageRequest = PageRequest.of(0, 10);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(repository.findAllByUserId(any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        Page<MedicationScheduleDTO> result = service.findAll(userId, pageRequest);
        assertThat(result.getPageable()).isEqualTo(pageRequest);
        assertThat(result.getContent()).contains(MedicationScheduleDTO.ofEntity(entity, medicationDTO));
    }

    @Test
    void findAll_usesRepository() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(TODAY.toLocalDate());
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before breakfast");
        PageRequest pageRequest = PageRequest.of(0, 10);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(repository.findAllByUserId(any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findAll(userId, pageRequest);
        verify(repository).findAllByUserId(userId, pageRequest);
    }

    @Test
    void findAll_retrievesMedication() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(TODAY.toLocalDate());
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before breakfast");
        PageRequest pageRequest = PageRequest.of(0, 10);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(repository.findAllByUserId(any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findAll(userId, pageRequest);
        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void findById_returnsResults() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(TODAY.toLocalDate());
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before breakfast");
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(repository.findByIdAndUserId(entity.getId(), userId)).thenReturn(Optional.of(entity));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        MedicationScheduleDTO result = service.findById(userId, entity.getId());
        assertThat(result).isEqualTo(MedicationScheduleDTO.ofEntity(entity, medicationDTO));
    }

    @Test
    void findById_usesRepository() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(TODAY.toLocalDate());
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before breakfast");
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(repository.findByIdAndUserId(entity.getId(), userId)).thenReturn(Optional.of(entity));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findById(userId, entity.getId());
        verify(repository).findByIdAndUserId(entity.getId(), userId);
    }

    @Test
    void findById_retrievesMedicationInfo() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(TODAY.toLocalDate());
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before breakfast");
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        when(repository.findByIdAndUserId(entity.getId(), userId)).thenReturn(Optional.of(entity));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.findById(userId, entity.getId());
        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void create_returnsResult() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, null, interval, time, "Before breakfast");
        when(userFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.save(any())).thenAnswer(returnsFirstArg());

        MedicationScheduleDTO result = service.create(userId, request);
        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new MedicationScheduleDTO(
                null,
                medicationDTO,
                quantity,
                new MedicationSchedulePeriodDTO(startingAt, null),
                interval,
                time,
                "Before breakfast"
            ));
    }

    @Test
    void create_usesRepository() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, null, interval, time, "Before breakfast");
        when(userFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.save(any())).thenAnswer(returnsFirstArg());

        service.create(userId, request);
        verify(repository).save(anyEntity.capture());
        assertThat(anyEntity.getValue())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(MedicationScheduleEntity.of(
                medicationId,
                userId,
                quantity,
                MedicationSchedulePeriod.ofUnboundedEnd(startingAt),
                interval,
                time,
                "Before breakfast"
            ));
        assertThat(anyEntity.getValue().getId()).isNotNull();
    }

    @Test
    void create_validatesMedication() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, null, interval, time, "Before breakfast");
        when(medicationFacade.existsById(any())).thenReturn(false);

        assertThatExceptionOfType(InvalidMedicationScheduleException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessage("Medication is not valid");
        verify(medicationFacade).existsById(medicationId);
        verifyNoInteractions(repository);
    }

    @Test
    void create_validatesUser() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, null, interval, time, "Before breakfast");
        when(userFacade.existsById(userId)).thenReturn(false);
        when(medicationFacade.existsById(any())).thenReturn(true);

        assertThatExceptionOfType(InvalidMedicationScheduleException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessage("User is not valid");
        verify(userFacade).existsById(userId);
        verifyNoInteractions(repository);
    }

    @Test
    void create_retrievesMedicationInfo() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, null, interval, time, "Before breakfast");
        when(userFacade.existsById(userId)).thenReturn(true);
        when(medicationFacade.existsById(any())).thenReturn(true);
        when(medicationFacade.findByIdOrDummy(medicationId)).thenReturn(medicationDTO);
        when(repository.save(any())).thenAnswer(returnsFirstArg());

        service.create(userId, request);
        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void update_returnsResult() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal oldQuantity = new BigDecimal("2");
        BigDecimal newQuantity = new BigDecimal("1");
        LocalDate newStartingAt = TODAY.toLocalDate();
        LocalDate oldStartingAt = TODAY.toLocalDate().plusDays(1);
        Period oldInterval = Period.ofDays(2);
        Period newInterval = Period.ofDays(1);
        LocalTime oldTime = LocalTime.of(7, 0);
        LocalTime newTime = LocalTime.of(8, 0);
        MedicationSchedulePeriod oldPeriod = MedicationSchedulePeriod.ofUnboundedEnd(oldStartingAt);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, oldQuantity, oldPeriod, oldInterval, oldTime, "Before breakfast");
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(newQuantity, newStartingAt, null, newInterval, newTime, "After breakfast");
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        MedicationScheduleDTO result = service.update(userId, entity.getId(), request);
        assertThat(result)
            .isEqualTo(new MedicationScheduleDTO(
                entity.getId(),
                medicationDTO,
                newQuantity,
                new MedicationSchedulePeriodDTO(newStartingAt, null),
                newInterval,
                newTime,
                request.description()
            ));
    }

    @Test
    void update_updatesEntity() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal oldQuantity = new BigDecimal("2");
        BigDecimal newQuantity = new BigDecimal("1");
        LocalDate newStartingAt = TODAY.toLocalDate();
        LocalDate oldStartingAt = TODAY.toLocalDate().plusDays(1);
        Period oldInterval = Period.ofDays(2);
        Period newInterval = Period.ofDays(1);
        LocalTime oldTime = LocalTime.of(7, 0);
        LocalTime newTime = LocalTime.of(8, 0);
        MedicationSchedulePeriod oldPeriod = MedicationSchedulePeriod.ofUnboundedEnd(oldStartingAt);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, oldQuantity, oldPeriod, oldInterval, oldTime, "Before breakfast");
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(newQuantity, newStartingAt, null, newInterval, newTime, "After breakfast");
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.update(userId, entity.getId(), request);
        assertThat(entity)
            .usingRecursiveComparison()
            .isEqualTo(new MedicationScheduleEntity(
                entity.getId(),
                medicationId,
                userId,
                newQuantity,
                MedicationSchedulePeriod.ofUnboundedEnd(newStartingAt),
                newInterval,
                newTime,
                request.description(),
                List.of(),
                null,
                null
            ));
    }

    @Test
    void update_retrievesMedicationInfo() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal oldQuantity = new BigDecimal("2");
        BigDecimal newQuantity = new BigDecimal("1");
        LocalDate newStartingAt = TODAY.toLocalDate();
        LocalDate oldStartingAt = TODAY.toLocalDate().plusDays(1);
        Period oldInterval = Period.ofDays(2);
        Period newInterval = Period.ofDays(1);
        LocalTime oldTime = LocalTime.of(7, 0);
        LocalTime newTime = LocalTime.of(8, 0);
        MedicationSchedulePeriod oldPeriod = MedicationSchedulePeriod.ofUnboundedEnd(oldStartingAt);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, oldQuantity, oldPeriod, oldInterval, oldTime, "Before breakfast");
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(newQuantity, newStartingAt, null, newInterval, newTime, "After breakfast");
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.update(userId, entity.getId(), request);
        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void update_retrievesEntity() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal oldQuantity = new BigDecimal("2");
        BigDecimal newQuantity = new BigDecimal("1");
        LocalDate newStartingAt = TODAY.toLocalDate();
        LocalDate oldStartingAt = TODAY.toLocalDate().plusDays(1);
        Period oldInterval = Period.ofDays(2);
        Period newInterval = Period.ofDays(1);
        LocalTime oldTime = LocalTime.of(7, 0);
        LocalTime newTime = LocalTime.of(8, 0);
        MedicationSchedulePeriod oldPeriod = MedicationSchedulePeriod.ofUnboundedEnd(oldStartingAt);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, oldQuantity, oldPeriod, oldInterval, oldTime, "Before breakfast");
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(newQuantity, newStartingAt, null, newInterval, newTime, "After breakfast");
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        service.update(userId, entity.getId(), request);
        verify(repository).findByIdAndUserId(entity.getId(), userId);
    }

    @Test
    void update_throwsExceptionIfEntityNotFound() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal newQuantity = new BigDecimal("1");
        LocalDate newStartingAt = TODAY.toLocalDate();
        Period newInterval = Period.ofDays(1);
        LocalTime newTime = LocalTime.of(8, 0);
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(newQuantity, newStartingAt, null, newInterval, newTime, "After breakfast");

        assertThatExceptionOfType(MedicationScheduleNotFoundException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessage("Schedule with ID '" + id + "' was not found");
    }

    @Test
    void delete_usesRepository() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));

        service.delete(userId, entity.getId());
        verify(repository).findByIdAndUserId(entity.getId(), userId);
        verify(repository).delete(entity);
    }

    @Test
    void delete_throwsExceptionIfNotFound() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        assertThatExceptionOfType(MedicationScheduleNotFoundException.class)
            .isThrownBy(() -> service.delete(userId, id))
            .withMessage("Schedule with ID '" + id + "' was not found");
    }

    @Test
    void findEventsByDate_returnsResults() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        PageRequest pageRequest = PageRequest.of(0, 100);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        LocalDateTime completedDateTime = LocalDateTime.of(eventDate, LocalTime.of(8, 1));
        MedicationScheduleCompletedEventEntity eventEntity = entity.addCompletedEvent(eventDate, completedDateTime);
        when(repository.findAllByUserIdAndDate(any(), any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.findCompletedEventByIdAndEventDate(any(), any())).thenReturn(Optional.of(eventEntity));

        Collection<MedicationEventDTO> results = service.findEventsByDate(userId, eventDate);
        assertThat(results).contains(new MedicationEventDTO(
            entity.getId(),
            medicationDTO,
            quantity,
            LocalDateTime.of(eventDate, time),
            entity.getDescription(),
            completedDateTime
        ));
    }

    @Test
    void findEventsByDate_returnsResultsWithoutCompletedEvent() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        PageRequest pageRequest = PageRequest.of(0, 100);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        when(repository.findAllByUserIdAndDate(any(), any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);

        Collection<MedicationEventDTO> results = service.findEventsByDate(userId, eventDate);
        assertThat(results).contains(new MedicationEventDTO(
            entity.getId(),
            medicationDTO,
            quantity,
            LocalDateTime.of(eventDate, time),
            entity.getDescription(),
            null
        ));
    }

    @Test
    void findEventsByDate_usesRepository() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        PageRequest pageRequest = PageRequest.of(0, 100);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        LocalDateTime completedDateTime = LocalDateTime.of(eventDate, LocalTime.of(8, 1));
        MedicationScheduleCompletedEventEntity eventEntity = entity.addCompletedEvent(eventDate, completedDateTime);
        when(repository.findAllByUserIdAndDate(any(), any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.findCompletedEventByIdAndEventDate(any(), any())).thenReturn(Optional.of(eventEntity));

        service.findEventsByDate(userId, eventDate);
        verify(repository).findAllByUserIdAndDate(userId, eventDate, pageRequest);
        verify(repository).findCompletedEventByIdAndEventDate(entity.getId(), LocalDateTime.of(eventDate, time));
    }

    @Test
    void findEventsByDate_retrievesMedicationInfo() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        PageRequest pageRequest = PageRequest.of(0, 100);
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        LocalDateTime completedDateTime = LocalDateTime.of(eventDate, LocalTime.of(8, 1));
        MedicationScheduleCompletedEventEntity eventEntity = entity.addCompletedEvent(eventDate, completedDateTime);
        when(repository.findAllByUserIdAndDate(any(), any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.findCompletedEventByIdAndEventDate(any(), any())).thenReturn(Optional.of(eventEntity));

        service.findEventsByDate(userId, eventDate);
        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void findEventsByDate_excludesSchedulesNotHappeningOnEventDate() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        PageRequest pageRequest = PageRequest.of(0, 100);
        LocalDate eventDate = TODAY.toLocalDate().minusDays(1);
        when(repository.findAllByUserIdAndDate(any(), any(), any())).thenReturn(new PageImpl<>(List.of(entity), pageRequest, 1));

        Collection<MedicationEventDTO> results = service.findEventsByDate(userId, eventDate);
        assertThat(results).isEmpty();
        verify(repository).findAllByUserIdAndDate(userId, eventDate, pageRequest);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(medicationFacade);
    }

    @Test
    void completeEvent_returnsResult() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        UserInfoDTO userInfo = new UserInfoDTO(userId, "me@example.org", "", ZoneId.of("Europe/Brussels"));
        when(userFacade.findById(any())).thenReturn(userInfo);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));

        MedicationEventDTO result = service.completeEvent(userId, entity.getId(), eventDate);
        assertThat(result).isEqualTo(new MedicationEventDTO(
            entity.getId(),
            medicationDTO,
            quantity,
            LocalDateTime.of(eventDate, time),
            entity.getDescription(),
            LocalDateTime.of(2022, 10, 7, 12, 0, 0)
        ));
    }

    @Test
    void completeEvent_usesDefaultTimezoneIfNotFound() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        when(userFacade.findById(any())).thenThrow(new UserNotFoundException(userId));
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));

        MedicationEventDTO result = service.completeEvent(userId, entity.getId(), eventDate);
        assertThat(result).isEqualTo(new MedicationEventDTO(
            entity.getId(),
            medicationDTO,
            quantity,
            LocalDateTime.of(eventDate, time),
            entity.getDescription(),
            LocalDateTime.of(2022, 10, 7, 10, 0, 0)
        ));
    }

    @Test
    void completeEvent_addsEntity() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        UserInfoDTO userInfo = new UserInfoDTO(userId, "me@example.org", "", ZoneId.of("Europe/Brussels"));
        when(userFacade.findById(any())).thenReturn(userInfo);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));

        service.completeEvent(userId, entity.getId(), eventDate);
        assertThat(entity.getCompletedEvents())
            .first()
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(MedicationScheduleCompletedEventEntity.of(
                entity,
                eventDate,
                LocalDateTime.of(2022, 10, 7, 12, 0, 0)
            ));
    }

    @Test
    void completeEvent_usesRepository() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        UserInfoDTO userInfo = new UserInfoDTO(userId, "me@example.org", "", ZoneId.of("Europe/Brussels"));
        when(userFacade.findById(any())).thenReturn(userInfo);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));

        service.completeEvent(userId, entity.getId(), eventDate);
        verify(repository).findByIdAndUserId(entity.getId(), userId);
        verify(repository).findCompletedEventByIdAndEventDate(entity.getId(), LocalDateTime.of(eventDate, time));
        verify(repository).save(entity);
    }

    @Test
    void completeEvent_retrievesMedicationInfo() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        UserInfoDTO userInfo = new UserInfoDTO(userId, "me@example.org", "", ZoneId.of("Europe/Brussels"));
        when(userFacade.findById(any())).thenReturn(userInfo);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));

        service.completeEvent(userId, entity.getId(), eventDate);
        verify(medicationFacade).findByIdOrDummy(medicationId);
    }

    @Test
    void completeEvent_retrievesUserInfoForTimezone() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(UUID.randomUUID(), "ml");
        MedicationDTO medicationDTO = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        UserInfoDTO userInfo = new UserInfoDTO(userId, "me@example.org", "", ZoneId.of("Europe/Brussels"));
        when(userFacade.findById(any())).thenReturn(userInfo);
        when(medicationFacade.findByIdOrDummy(any())).thenReturn(medicationDTO);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));

        service.completeEvent(userId, entity.getId(), eventDate);
        verify(userFacade).findById(userId);
    }

    @Test
    void completeEvent_throwsExceptionIfEntityNotFound() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);

        assertThatExceptionOfType(MedicationScheduleNotFoundException.class)
            .isThrownBy(() -> service.completeEvent(userId, id, eventDate))
            .withMessage("Schedule with ID '" + id + "' was not found");
        verifyNoInteractions(medicationFacade, userFacade);
    }

    @Test
    void completeEvent_throwsExceptionIfNoEventOccuringOnEventDate() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        LocalDate eventDate = TODAY.toLocalDate().minusDays(1);
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));

        assertThatExceptionOfType(MedicationEventNotFoundException.class)
            .isThrownBy(() -> service.completeEvent(userId, entity.getId(), eventDate))
            .withMessage("This medication should not be taken at 2022-10-06");
        verifyNoInteractions(medicationFacade, userFacade);
    }

    @Test
    void completeEvent_throwsExceptionIfEventAlreadyCompletedForDate() {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = TODAY.toLocalDate();
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "After breakfast");
        LocalDate eventDate = TODAY.toLocalDate().plusDays(1);
        MedicationScheduleCompletedEventEntity eventEntity = entity.addCompletedEvent(eventDate, LocalDateTime.of(2022, 10, 7, 12, 0, 0));
        when(repository.findCompletedEventByIdAndEventDate(any(), any())).thenReturn(Optional.of(eventEntity));
        when(repository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(entity));

        assertThatExceptionOfType(InvalidMedicationEventException.class)
            .isThrownBy(() -> service.completeEvent(userId, entity.getId(), eventDate))
                .withMessage("This medication was already taken at this time");
        verify(repository).findByIdAndUserId(entity.getId(), userId);
        verifyNoInteractions(userFacade, medicationFacade);
    }
}