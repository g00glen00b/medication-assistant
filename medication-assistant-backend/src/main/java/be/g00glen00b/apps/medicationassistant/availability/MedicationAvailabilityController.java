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
    private final MedicationAvailabilityFacade facade;

    @GetMapping
    @ApiOperation(value = "Retrieve a list of the current authenticated user their available medication", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationAvailabilityDTOPage.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public Page<MedicationAvailabilityDTO> findAll(Pageable pageable, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return facade.findAllByUserId(authentication.getId(), pageable);
    }

    @PostMapping
    @ApiOperation(value = "Add a specific medication to the user their available medication", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationAvailabilityDTO.class),
        @ApiResponse(code = 400, message = "Bad request", response = MessageDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class)
    })
    public MedicationAvailabilityDTO create(@RequestBody CreateMedicationAvailabilityRequestDTO input, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return facade.create(authentication.getId(), input);
    }

    @PutMapping("/{id}/quantity")
    @ApiOperation(value = "Update the quantity of a user their available medication", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationAvailabilityDTO.class),
        @ApiResponse(code = 400, message = "Bad request", response = MessageDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class),
        @ApiResponse(code = 404, message = "Not found", response = MessageDTO.class)
    })
    public MedicationAvailabilityDTO updateQuantity(@PathVariable UUID id, @RequestBody UpdateMedicationAvailabilityQuantityInputDTO input, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return facade.updateQuantity(authentication.getId(), id, input);
    }

    @PutMapping("/{id}/increase")
    @ApiOperation(value = "Increase the quantity of a user their available medication by one", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationAvailabilityDTO.class),
        @ApiResponse(code = 400, message = "Bad request", response = MessageDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class),
        @ApiResponse(code = 404, message = "Not found", response = MessageDTO.class)
    })
    public MedicationAvailabilityDTO increaseQuantity(@PathVariable UUID id, @RequestBody @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return facade.increaseQuantity(authentication.getId(), id);
    }

    @PutMapping("/{id}/decrease")
    @ApiOperation(value = "Decrease the quantity of a user their available medication by one", authorizations = @Authorization("basicAuth"))
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = MedicationAvailabilityDTO.class),
        @ApiResponse(code = 400, message = "Bad request", response = MessageDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = MessageDTO.class),
        @ApiResponse(code = 404, message = "Not found", response = MessageDTO.class)
    })
    public MedicationAvailabilityDTO decreaseQuantity(@PathVariable UUID id, @AuthenticationPrincipal UserAuthenticationInfoDTO authentication) {
        return facade.decreaseQuantity(authentication.getId(), id);
    }

    private interface MedicationAvailabilityDTOPage extends Page<MedicationAvailabilityDTO> {}
}
