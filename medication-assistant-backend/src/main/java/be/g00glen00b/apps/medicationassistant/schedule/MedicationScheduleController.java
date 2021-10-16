package be.g00glen00b.apps.medicationassistant.schedule;

import be.g00glen00b.apps.medicationassistant.authentication.UserAuthenticationInfoDTO;
import be.g00glen00b.apps.medicationassistant.core.MessageDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
class MedicationScheduleController {
    private final MedicationScheduleFacade scheduleFacade;
    private final MedicationScheduleEventFacade eventFacade;

    @GetMapping
    @ApiOperation(value = "Retrieve a list of the current authenticated user their schedule", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationSchedulePageDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public Page<MedicationScheduleDTO> findAll(Pageable pageable, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return scheduleFacade.findAllByUserId(authentication.getId(), pageable);
    }

    @Transactional
    @PostMapping
    @ApiOperation(value = "Create a new medication schedule for the current authenticated user", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationScheduleDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public MedicationScheduleDTO create(@RequestBody CreateMedicationScheduleRequestDTO request, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        MedicationScheduleDTO schedule = scheduleFacade.create(authentication.getId(), request);
        eventFacade.createNextEvent(schedule.getId(), authentication.getId());
        return schedule;
    }

    @GetMapping("/event")
    @ApiOperation(value = "Retrieve a list of the current authenticated user their active medication events", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationScheduleEventPageDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public Page<MedicationScheduleEventDTO> findAllEvents(Pageable pageable, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return eventFacade.findAllActiveByUserId(authentication.getId(), pageable);
    }

    @PostMapping("/{scheduleId}/event/complete")
    @ApiOperation(value = "Complete an event for a user", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success with new event", response = MedicationScheduleEventDTO.class),
        @ApiResponse(code = 204, message = "Success without new event"),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public MedicationScheduleEventDTO complete(@PathVariable UUID scheduleId, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return eventFacade.createNextEvent(scheduleId, authentication.getId());
    }

    @Transactional
    @DeleteMapping("/{scheduleId}")
    @ApiOperation(value = "Delete a schedule for a user", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Success"),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class),
        @ApiResponse(code = 404, message = "Not found", response = MessageDTO.class)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID scheduleId, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        eventFacade.deleteBySchedule(scheduleId, authentication.getId());
        scheduleFacade.delete(authentication.getId(), scheduleId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(LastMedicationScheduleEventException.class)
    public void noContentWhenLastEvent() { }

    private interface MedicationSchedulePageDTO extends Page<MedicationScheduleDTO> {}
    private interface MedicationScheduleEventPageDTO extends Page<MedicationScheduleEventDTO> {}

}
