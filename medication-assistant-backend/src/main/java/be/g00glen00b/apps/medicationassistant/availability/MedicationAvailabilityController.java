package be.g00glen00b.apps.medicationassistant.availability;

import be.g00glen00b.apps.medicationassistant.authentication.UserAuthenticationInfoDTO;
import be.g00glen00b.apps.medicationassistant.core.MessageDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class MedicationAvailabilityController {
    private final MedicationAvailabilityService service;

    @GetMapping
    @ApiOperation(value = "Retrieve a list of the current authenticated user their available medication", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationAvailabilityDTOPage.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public Page<MedicationAvailabilityDTO> findAll(Pageable pageable, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return service.findAllByUserId(authentication.getId(), pageable);
    }

    @PutMapping("/{medicationId}")
    @ApiOperation(value = "Update the current user their available quantity of a specific medication", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationAvailabilityDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public MedicationAvailabilityDTO createOrUpdate(@PathVariable UUID medicationId, @RequestBody MedicationAvailabilityInputDTO input, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return service.updateAvailability(authentication.getId(), medicationId, input);
    }

    private interface MedicationAvailabilityDTOPage extends Page<MedicationAvailabilityDTO> {}
}
