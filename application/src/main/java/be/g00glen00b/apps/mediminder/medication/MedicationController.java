package be.g00glen00b.apps.mediminder.medication;

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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/medication")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationFacade facade;

    @GetMapping
    @Operation(summary = "Retrieve a list of medication by their partial or full name", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationDTOPage.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    public Page<MedicationDTO> findAll(@RequestParam(required = false, defaultValue = "") String search, @ParameterObject Pageable pageable) {
        return facade.findAll(search, pageable);
    }

    @GetMapping("/quantity-type")
    @Operation(summary = "Retrieve a list of quantity types", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationQuantityTypeDTOPage.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    public Page<MedicationQuantityTypeDTO> findAllQuantityTypes(@ParameterObject Pageable pageable) {
        return facade.findAllQuantityTypes(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a specific medication by its unique identifier", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    public MedicationDTO findById(@PathVariable UUID id) {
        return facade.findById(id);
    }

    @PutMapping
    @Operation(summary = "Retrieve or create medication by its name", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = MedicationDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = MessageDTO.class)))
    })
    public MedicationDTO findOrCreate(@RequestBody CreateMedicationRequestDTO input) {
        return facade.findOrCreate(input);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MedicationNotFoundException.class)
    public MessageDTO handleMedicationNotFound(MedicationNotFoundException ex) {
        return new MessageDTO(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidMedicationException.class)
    public MessageDTO handleInvalidMedication(InvalidMedicationException ex) {
        return new MessageDTO(ex.getMessage());
    }

    private interface MedicationDTOPage extends Page<MedicationDTO> {}

    private interface MedicationQuantityTypeDTOPage extends Page<MedicationQuantityTypeDTO> {}
}
