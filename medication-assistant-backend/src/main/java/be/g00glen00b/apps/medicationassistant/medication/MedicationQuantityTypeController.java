package be.g00glen00b.apps.medicationassistant.medication;

import be.g00glen00b.apps.medicationassistant.core.MessageDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quantity-type")
@RequiredArgsConstructor
public class MedicationQuantityTypeController {
    private final MedicationQuantityTypeService service;

    @GetMapping
    @ApiOperation(value = "Retrieve a list of possible quantities to be used with medications", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationQuantityTypePage.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public Page<MedicationQuantityTypeDTO> findAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    private interface MedicationQuantityTypePage extends Page<MedicationQuantityTypeDTO> {}
}
