package be.g00glen00b.apps.mediminder.medication;

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

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicationController.class)
@Import(WebSecurityConfiguration.class)
class MedicationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MedicationFacade facade;

    @Test
    void findAll_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(id, "Hydrocortisone", quantityType);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MedicationDTO> page = new PageImpl<>(List.of(medication), pageRequest, 1);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findAll(any(), any())).thenReturn(page);

        mockMvc
            .perform(get("/api/medication")
                .param("search", "hydro")
                .param("page", "0")
                .param("size", "10")
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(id.toString())));
        verify(facade).findAll("hydro", pageRequest);
    }

    @Test
    void findAll_401() throws Exception {
        mockMvc
            .perform(get("/api/medication")
                .param("search", "hydro")
                .param("page", "0")
                .param("size", "10")
                .with(anonymous()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void findAllQuantityTypes_200() throws Exception {
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MedicationQuantityTypeDTO> page = new PageImpl<>(List.of(quantityType), pageRequest, 1);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findAllQuantityTypes(any())).thenReturn(page);

        mockMvc
            .perform(get("/api/medication/quantity-type")
                .param("page", "0")
                .param("size", "10")
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(quantityTypeId.toString())));
        verify(facade).findAllQuantityTypes(pageRequest);
    }

    @Test
    void findAllQuantityTypes_401() throws Exception {
        mockMvc
            .perform(get("/api/medication/quantity-type")
                .param("page", "0")
                .param("size", "10")
                .with(anonymous()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void findById_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(id, "Hydrocortisone", quantityType);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findById(any())).thenReturn(medication);

        mockMvc
            .perform(get("/api/medication/{id}", id)
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())));
        verify(facade).findById(id);
    }

    @Test
    void findById_401() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc
            .perform(get("/api/medication/{id}", id)
                .with(anonymous()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void findById_404() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findById(any())).thenThrow(new MedicationNotFoundException(id));

        mockMvc
            .perform(get("/api/medication/{id}", id)
                .with(user(user)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Medication with ID '" + id + "' was not found")));
        verify(facade).findById(id);
    }

    @Test
    void findOrCreate_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(id, "Hydrocortisone", quantityType);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findOrCreate(any())).thenReturn(medication);

        mockMvc
            .perform(put("/api/medication", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Hydrocortisone",
                        "quantityTypeId": "%s"
                    }
                """.formatted(quantityTypeId))
                .with(csrf())
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())));
        verify(facade).findOrCreate(new CreateMedicationRequestDTO("Hydrocortisone", quantityTypeId));
    }

    @Test
    void findOrCreate_400() throws Exception {
        UUID id = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findOrCreate(any())).thenThrow(new InvalidMedicationException("Validation failed"));

        mockMvc
            .perform(put("/api/medication", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Hydrocortisone",
                        "quantityTypeId": "%s"
                    }
                """.formatted(quantityTypeId))
                .with(csrf())
                .with(user(user)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void findOrCreate_401() throws Exception {
        UUID id = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();

        mockMvc
            .perform(put("/api/medication", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Hydrocortisone",
                        "quantityTypeId": "%s"
                    }
                """.formatted(quantityTypeId))
                .with(csrf()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void findOrCreate_403() throws Exception {
        UUID id = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(put("/api/medication", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Hydrocortisone",
                        "quantityTypeId": "%s"
                    }
                """.formatted(quantityTypeId))
                .with(user(user)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }
}