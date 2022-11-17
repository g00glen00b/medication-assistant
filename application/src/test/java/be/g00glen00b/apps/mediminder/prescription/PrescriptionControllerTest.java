package be.g00glen00b.apps.mediminder.prescription;

import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationQuantityTypeDTO;
import be.g00glen00b.apps.mediminder.user.UserAuthentication;
import be.g00glen00b.apps.mediminder.user.configuration.WebSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PrescriptionController.class)
@Import(WebSecurityConfiguration.class)
class PrescriptionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PrescriptionFacade facade;

    @Test
    void findAll_200() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "capsule");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        PrescriptionDTO prescription = new PrescriptionDTO(medication, new BigDecimal("10"), new BigDecimal("3"), new BigDecimal("7"), new BigDecimal("2"), new BigDecimal("4"));
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.calculatePrescriptions(any(), any())).thenReturn(List.of(prescription));

        mockMvc
            .perform(get("/api/prescription/2022-11-10")
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].medication.id", is(medicationId.toString())));
        verify(facade).calculatePrescriptions(userId, LocalDate.of(2022, 11, 10));
    }

    @Test
    void findAll_401() throws Exception {
        mockMvc
            .perform(get("/api/prescription/2022-11-10")
                .with(anonymous()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }
}