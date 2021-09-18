package be.g00glen00b.apps.medicationassistant.medication;

import be.g00glen00b.apps.medicationassistant.core.MessageDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/query")
    @ApiOperation(value = "Retrieve a list of medication by their partial or full name", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationDTOPage.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public Page<MedicationDTO> findAll(@RequestParam(required = false, defaultValue = "") String search, Pageable pageable) {
        return facade.findAll(search, pageable);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Retrieve a specific medication by its unique identifier", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class),
        @ApiResponse(code = 404, message = "Not found", response = MessageDTO.class)
    })
    public MedicationDTO findById(@PathVariable UUID id) {
        return facade
            .findById(id)
            .orElseThrow(() -> new MedicationNotFoundException("Medication with ID '" + id + "' does not exist"));
    }

    @PutMapping
    @ApiOperation(value = "Retrieve or create medication by its name", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public MedicationDTO findOrCreate(@RequestBody MedicationInputDTO input) {
        return facade.findOrCreate(input);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MedicationNotFoundException.class)
    public MessageDTO handleInvalidUser(MedicationNotFoundException ex) {
        return new MessageDTO(ex.getMessage());
    }

    private interface MedicationDTOPage extends Page<MedicationDTO> {}
}
