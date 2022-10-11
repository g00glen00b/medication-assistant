package be.g00glen00b.apps.mediminder.availability;

import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationQuantityTypeDTO;
import be.g00glen00b.apps.mediminder.user.UserAuthentication;
import be.g00glen00b.apps.mediminder.user.configuration.WebSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicationAvailabilityController.class)
@Import(WebSecurityConfiguration.class)
class MedicationAvailabilityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MedicationAvailabilityFacade facade;

    @Test
    void findAll_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate expiryDate = LocalDate.of(2022, 10, 31);
        MedicationAvailabilityDTO availability = new MedicationAvailabilityDTO(id, medication, new BigDecimal("20"), new BigDecimal("60"), expiryDate);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityDTO> page = new PageImpl<>(List.of(availability), pageRequest, 1);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findAllNonEmptyNonExpiredByUserId(any(), any())).thenReturn(page);

        mockMvc
            .perform(get("/api/availability")
                .param("page", "0")
                .param("size", "10")
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(id.toString())));
        verify(facade).findAllNonEmptyNonExpiredByUserId(userId, pageRequest);
    }

    @Test
    void findAll_401() throws Exception {
        mockMvc
            .perform(get("/api/availability")
                .param("page", "0")
                .param("size", "10")
                .with(anonymous()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void findById_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate expiryDate = LocalDate.of(2022, 10, 31);
        MedicationAvailabilityDTO availability = new MedicationAvailabilityDTO(id, medication, new BigDecimal("20"), new BigDecimal("60"), expiryDate);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findById(any(), any())).thenReturn(availability);

        mockMvc
            .perform(get("/api/availability/{id}", id)
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())));
        verify(facade).findById(userId, id);
    }

    @Test
    void findById_401() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc
            .perform(get("/api/availability/{id}", id)
                .param("page", "0")
                .param("size", "10")
                .with(anonymous()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void findById_404() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findById(any(), any())).thenThrow(new MedicationAvailabilityNotFoundException(id));

        mockMvc
            .perform(get("/api/availability/{id}", id)
                .with(user(user)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Medication availability with ID '" + id + "' was not found")));
    }

    @Test
    void create_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate expiryDate = LocalDate.of(2022, 10, 31);
        MedicationAvailabilityDTO availability = new MedicationAvailabilityDTO(id, medication, new BigDecimal("20"), new BigDecimal("60"), expiryDate);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.create(any(), any())).thenReturn(availability);

        mockMvc
            .perform(post("/api/availability")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "medicationName": "Hydrocortisone",
                        "quantityTypeId": "%s",
                        "quantity": "1",
                        "initialQuantity": "10",
                        "expiryDate": "2022-10-31"
                    }
                    """.formatted(quantityTypeId)
                )
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())));
        verify(facade).create(userId, new CreateMedicationAvailabilityRequestDTO(
            "Hydrocortisone",
            quantityTypeId,
            new BigDecimal("1"),
            new BigDecimal("10"),
            LocalDate.of(2022, 10, 31)));
    }

    @Test
    void create_401() throws Exception {
        UUID quantityTypeId = UUID.randomUUID();

        mockMvc
            .perform(post("/api/availability")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "medicationName": "Hydrocortisone",
                        "quantityTypeId": "%s",
                        "quantity": "1",
                        "initialQuantity": "10",
                        "expiryDate": "2022-10-31"
                    }
                    """.formatted(quantityTypeId)
                )
                .with(anonymous())
                .with(csrf()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void create_403Csrf() throws Exception {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(post("/api/availability")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "medicationName": "Hydrocortisone",
                        "quantityTypeId": "%s",
                        "quantity": "1",
                        "initialQuantity": "10",
                        "expiryDate": "2022-10-31"
                    }
                    """.formatted(quantityTypeId)
                )
                .with(user(user)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }

    @Test
    void create_400() throws Exception {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.create(any(), any())).thenThrow(new InvalidMedicationAvailabilityException("Validation failed"));

        mockMvc
            .perform(post("/api/availability")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "medicationName": "Hydrocortisone",
                        "quantityTypeId": "%s",
                        "quantity": "1",
                        "initialQuantity": "10",
                        "expiryDate": "2022-10-31"
                    }
                    """.formatted(quantityTypeId)
                )
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void update_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate expiryDate = LocalDate.of(2022, 10, 31);
        MedicationAvailabilityDTO availability = new MedicationAvailabilityDTO(id, medication, new BigDecimal("20"), new BigDecimal("60"), expiryDate);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.update(any(), any(), any())).thenReturn(availability);

        mockMvc
            .perform(put("/api/availability/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "quantity": "1",
                        "initialQuantity": "10",
                        "expiryDate": "2022-10-31"
                    }
                    """
                )
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())));
        verify(facade).update(userId, id, new UpdateMedicationAvailabilityRequestDTO(
            new BigDecimal("1"),
            new BigDecimal("10"),
            LocalDate.of(2022, 10, 31)));
    }

    @Test
    void update_401() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc
            .perform(put("/api/availability/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "quantity": "1",
                        "initialQuantity": "10",
                        "expiryDate": "2022-10-31"
                    }
                    """
                )
                .with(anonymous())
                .with(csrf()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void update_403Csrf() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(put("/api/availability/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "quantity": "1",
                        "initialQuantity": "10",
                        "expiryDate": "2022-10-31"
                    }
                    """
                )
                .with(user(user)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }

    @Test
    void update_400() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.update(any(), any(), any())).thenThrow(new InvalidMedicationAvailabilityException("Validation failed"));

        mockMvc
            .perform(put("/api/availability/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "quantity": "1",
                        "initialQuantity": "10",
                        "expiryDate": "2022-10-31"
                    }
                    """
                )
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void update_404() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.update(any(), any(), any())).thenThrow(new MedicationAvailabilityNotFoundException(id));

        mockMvc
            .perform(put("/api/availability/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "quantity": "1",
                        "initialQuantity": "10",
                        "expiryDate": "2022-10-31"
                    }
                    """
                )
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Medication availability with ID '" + id + "' was not found")));
    }

    @Test
    void delete_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(delete("/api/availability/{id}", id)
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isOk());
        verify(facade).delete(userId, id);
    }

    @Test
    void delete_401() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc
            .perform(delete("/api/availability/{id}", id)
                .with(anonymous())
                .with(csrf()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void delete_403Csrf() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(delete("/api/availability/{id}", id)
                .with(user(user)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }

    @Test
    void delete_404() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        doThrow(new MedicationAvailabilityNotFoundException(id)).when(facade).delete(any(), any());

        mockMvc
            .perform(delete("/api/availability/{id}", id)
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Medication availability with ID '" + id + "' was not found")));
    }
}