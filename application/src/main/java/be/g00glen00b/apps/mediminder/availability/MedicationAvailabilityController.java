package be.g00glen00b.apps.mediminder.availability;

import be.g00glen00b.apps.mediminder.user.UserAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class MedicationAvailabilityController {
    private final MedicationAvailabilityFacade facade;

    @Operation(summary = "Retrieve the user their available medication", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationAvailabilityPageDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @GetMapping
    public Page<MedicationAvailabilityDTO> findAll(@AuthenticationPrincipal UserAuthentication authentication, @ParameterObject Pageable pageable) {
        UUID userId = authentication.getId();
        return facade.findAllNonEmptyNonExpiredByUserId(userId, pageable);
    }

    @Operation(summary = "Retrieve one of the user their available medication by its ID", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationAvailabilityPageDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @GetMapping("/{id}")
    public MedicationAvailabilityDTO findById(@PathVariable UUID id, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        return facade.findById(userId, id);
    }

    @Operation(summary = "Create a new available medication for a user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationAvailabilityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @PostMapping
    public MedicationAvailabilityDTO create(@RequestBody CreateMedicationAvailabilityRequestDTO request, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        return facade.create(userId, request);
    }

    @Operation(summary = "Update the available medication for a user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationAvailabilityDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @PutMapping("/{id}")
    public MedicationAvailabilityDTO update(@PathVariable UUID id, @RequestBody UpdateMedicationAvailabilityRequestDTO request, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        return facade.update(userId, id, request);
    }

    @Operation(summary = "Delete the available medication for a user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal UserAuthentication authentication) {
        UUID userId = authentication.getId();
        facade.delete(userId, id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(be.g00glen00b.apps.mediminder.availability.InvalidMedicationAvailabilityException.class)
    public MessageDTO handleInvalidAvailability(InvalidMedicationAvailabilityException ex) {
        return new MessageDTO(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MedicationAvailabilityNotFoundException.class)
    public MessageDTO handleAvailabilityNotFound(MedicationAvailabilityNotFoundException ex) {
        return new MessageDTO(ex.getMessage());
    }

    private interface MedicationAvailabilityPageDTO extends Page<MedicationAvailabilityDTO> {}

}
