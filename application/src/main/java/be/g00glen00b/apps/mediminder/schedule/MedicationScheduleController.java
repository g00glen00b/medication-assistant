package be.g00glen00b.apps.mediminder.schedule;

import be.g00glen00b.apps.mediminder.user.UserAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class MedicationScheduleController {
    private final MedicationScheduleFacade facade;

    @Operation(summary = "Retrieve the user their scheduled medication intakes", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationScheduleDTOPage.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @GetMapping
    public Page<MedicationScheduleDTO> findAll(@AuthenticationPrincipal UserAuthentication authentication, Pageable pageable) {
        UUID userId = authentication.getId();
        return facade.findAll(userId, pageable);
    }

    @Operation(summary = "Retrieve a specific scheduled medication intake for a user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationScheduleDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @GetMapping("/{id}")
    public MedicationScheduleDTO findById(@PathVariable UUID id, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        return facade.findById(userId, id);
    }

    @Operation(summary = "Create a new scheduled medication intake for a user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationScheduleDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @PostMapping
    public MedicationScheduleDTO create(@RequestBody CreateMedicationScheduleRequestDTO request, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        return facade.create(userId, request);
    }

    @Operation(summary = "Update an existing scheduled medication intake for a user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationScheduleDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @PutMapping("/{id}")
    public MedicationScheduleDTO update(@PathVariable UUID id, @RequestBody UpdateMedicationScheduleRequestDTO request, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        return facade.update(userId, id, request);
    }

    @Operation(summary = "Delete an existing scheduled medication intake for a user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        facade.delete(userId, id);
    }

    @Operation(summary = "Retrieve the user their medication intake events for a specific day", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationEventDTO[].class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @GetMapping("/event/{date}")
    public Collection<MedicationEventDTO> findAllEvents(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        return facade.findEventsByDate(userId, date);
    }

    @Operation(summary = "Complete a specific medication intake event for a user on a given day", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationEventDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
    })
    @PostMapping("/{id}/event/{date}")
    public MedicationEventDTO completeEvent(@PathVariable UUID id, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        return facade.completeEvent(userId, id, date);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidMedicationScheduleException.class)
    public MessageDTO handleInvalidSchedule(InvalidMedicationScheduleException ex) {
        return new MessageDTO(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidMedicationEventException.class)
    public MessageDTO handleInvalidEvent(InvalidMedicationEventException ex) {
        return new MessageDTO(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MedicationScheduleNotFoundException.class)
    public MessageDTO handleScheduleNotFound(MedicationScheduleNotFoundException ex) {
        return new MessageDTO(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MedicationEventNotFoundException.class)
    public MessageDTO handleEventNotFound(MedicationEventNotFoundException ex) {
        return new MessageDTO(ex.getMessage());
    }

    private interface MedicationScheduleDTOPage extends Page<MedicationScheduleDTO> {}
}
