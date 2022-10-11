package be.g00glen00b.apps.mediminder.schedule;

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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicationScheduleController.class)
@Import(WebSecurityConfiguration.class)
class MedicationScheduleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MedicationScheduleFacade facade;

    @Test
    void findAll_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationSchedulePeriodDTO period = new MedicationSchedulePeriodDTO(LocalDate.of(2022, 10, 1), null);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleDTO schedule = new MedicationScheduleDTO(id, medication, new BigDecimal("1"), period, interval, time, "Before breakfast");
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MedicationScheduleDTO> page = new PageImpl<>(List.of(schedule), pageRequest, 1);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findAll(any(), any())).thenReturn(page);

        mockMvc
            .perform(get("/api/schedule")
                .param("page", "0")
                .param("size", "10")
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(id.toString())))
            .andExpect(jsonPath("$.content[0].period.startingAt", is("2022-10-01")))
            .andExpect(jsonPath("$.content[0].interval", is("P1D")));
        verify(facade).findAll(userId, pageRequest);
    }

    @Test
    void findAll_401() throws Exception {
        mockMvc
            .perform(get("/api/schedule")
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
        MedicationSchedulePeriodDTO period = new MedicationSchedulePeriodDTO(LocalDate.of(2022, 10, 1), null);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleDTO schedule = new MedicationScheduleDTO(id, medication, new BigDecimal("1"), period, interval, time, "Before breakfast");
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findById(any(), any())).thenReturn(schedule);

        mockMvc
            .perform(get("/api/schedule/{id}", id)
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())))
            .andExpect(jsonPath("$.period.startingAt", is("2022-10-01")))
            .andExpect(jsonPath("$.interval", is("P1D")));
        verify(facade).findById(userId, id);
    }

    @Test
    void findById_401() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc
            .perform(get("/api/schedule/{id}", id)
                .with(anonymous()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void findById_404() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findById(any(), any())).thenThrow(new MedicationScheduleNotFoundException(id));

        mockMvc
            .perform(get("/api/schedule/{id}", id)
                .with(user(user)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Schedule with ID '" + id + "' was not found")));
    }

    @Test
    void create_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate startingAt = LocalDate.of(2022, 10, 1);
        MedicationSchedulePeriodDTO period = new MedicationSchedulePeriodDTO(startingAt, null);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleDTO schedule = new MedicationScheduleDTO(id, medication, new BigDecimal("1"), period, interval, time, "Before breakfast");
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.create(any(), any())).thenReturn(schedule);

        mockMvc
            .perform(post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "medicationId": "%s",
                    "quantity": 1,
                    "startingAt": "2022-10-01",
                    "endingAtInclusive": null,
                    "interval": "P1D",
                    "time": "08:00",
                    "description": "Before breakfast"
                }
                """.formatted(medicationId))
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())))
            .andExpect(jsonPath("$.period.startingAt", is("2022-10-01")))
            .andExpect(jsonPath("$.interval", is("P1D")));
        verify(facade).create(userId, new CreateMedicationScheduleRequestDTO(medicationId, new BigDecimal("1"), startingAt, null, interval, time, "Before breakfast"));
    }

    @Test
    void create_400() throws Exception {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.create(any(), any())).thenThrow(new InvalidMedicationScheduleException("Validation failed"));

        mockMvc
            .perform(post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "medicationId": "%s",
                    "quantity": 1,
                    "startingAt": "2022-10-01",
                    "endingAtInclusive": null,
                    "interval": "P1D",
                    "time": "08:00",
                    "description": "Before breakfast"
                }
                """.formatted(medicationId))
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void create_401() throws Exception {
        UUID medicationId = UUID.randomUUID();

        mockMvc
            .perform(post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "medicationId": "%s",
                    "quantity": 1,
                    "startingAt": "2022-10-01",
                    "endingAtInclusive": null,
                    "interval": "P1D",
                    "time": "08:00",
                    "description": "Before breakfast"
                }
                """.formatted(medicationId))
                .with(anonymous())
                .with(csrf()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void create_403() throws Exception {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "medicationId": "%s",
                    "quantity": 1,
                    "startingAt": "2022-10-01",
                    "endingAtInclusive": null,
                    "interval": "P1D",
                    "time": "08:00",
                    "description": "Before breakfast"
                }
                """.formatted(medicationId))
                .with(user(user)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }

    @Test
    void update_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDate startingAt = LocalDate.of(2022, 10, 1);
        MedicationSchedulePeriodDTO period = new MedicationSchedulePeriodDTO(startingAt, null);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleDTO schedule = new MedicationScheduleDTO(id, medication, new BigDecimal("1"), period, interval, time, "Before breakfast");
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.update(any(), any(), any())).thenReturn(schedule);

        mockMvc
            .perform(put("/api/schedule/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "quantity": 1,
                    "startingAt": "2022-10-01",
                    "endingAtInclusive": null,
                    "interval": "P1D",
                    "time": "08:00",
                    "description": "Before breakfast"
                }
                """)
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())))
            .andExpect(jsonPath("$.period.startingAt", is("2022-10-01")))
            .andExpect(jsonPath("$.interval", is("P1D")));
        verify(facade).update(userId, id, new UpdateMedicationScheduleRequestDTO(new BigDecimal("1"), startingAt, null, interval, time, "Before breakfast"));
    }

    @Test
    void update_400() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.update(any(), any(), any())).thenThrow(new InvalidMedicationScheduleException("Validation failed"));

        mockMvc
            .perform(put("/api/schedule/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "quantity": 1,
                    "startingAt": "2022-10-01",
                    "endingAtInclusive": null,
                    "interval": "P1D",
                    "time": "08:00",
                    "description": "Before breakfast"
                }
                """)
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void update_401() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc
            .perform(put("/api/schedule/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "quantity": 1,
                    "startingAt": "2022-10-01",
                    "endingAtInclusive": null,
                    "interval": "P1D",
                    "time": "08:00",
                    "description": "Before breakfast"
                }
                """)
                .with(anonymous())
                .with(csrf()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void update_403() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(put("/api/schedule/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "quantity": 1,
                    "startingAt": "2022-10-01",
                    "endingAtInclusive": null,
                    "interval": "P1D",
                    "time": "08:00",
                    "description": "Before breakfast"
                }
                """)
                .with(user(user)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }

    @Test
    void update_404() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.update(any(), any(), any())).thenThrow(new MedicationScheduleNotFoundException(id));

        mockMvc
            .perform(put("/api/schedule/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "quantity": 1,
                    "startingAt": "2022-10-01",
                    "endingAtInclusive": null,
                    "interval": "P1D",
                    "time": "08:00",
                    "description": "Before breakfast"
                }
                """)
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Schedule with ID '" + id + "' was not found")));
    }

    @Test
    void delete_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(delete("/api/schedule/{id}", id)
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isOk());
        verify(facade).delete(userId, id);
    }

    @Test
    void delete_401() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc
            .perform(delete("/api/schedule/{id}", id)
                .with(anonymous())
                .with(csrf()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void delete_403() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(delete("/api/schedule/{id}", id)
                .with(user(user)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }

    @Test
    void delete_404() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        doThrow(new MedicationScheduleNotFoundException(id)).when(facade).delete(any(), any());

        mockMvc
            .perform(delete("/api/schedule/{id}", id)
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Schedule with ID '" + id + "' was not found")));
    }

    @Test
    void findAllEvents_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        MedicationEventDTO event = new MedicationEventDTO(id, medication, new BigDecimal(1), LocalDateTime.of(2022, 10, 1, 8, 0, 0), "Before breakfast", null);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findEventsByDate(any(), any())).thenReturn(List.of(event));

        mockMvc
            .perform(get("/api/schedule/event/{date}", "2022-10-01")
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].scheduleId", is(id.toString())))
            .andExpect(jsonPath("$.[0].eventDate", is("2022-10-01T08:00:00")));
        verify(facade).findEventsByDate(userId, LocalDate.of(2022, 10, 1));
    }

    @Test
    void findAllEvents_401() throws Exception {
        mockMvc
            .perform(get("/api/schedule/event/{date}", "2022-10-01"))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void completeEvent_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "Capsules");
        MedicationDTO medication = new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
        LocalDateTime completedDate = LocalDateTime.of(2022, 10, 1, 8, 1);
        LocalDateTime eventDate = LocalDateTime.of(2022, 10, 1, 8, 0);
        MedicationEventDTO event = new MedicationEventDTO(id, medication, new BigDecimal("1"), eventDate, "Before breakfast", completedDate);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.completeEvent(any(), any(), any())).thenReturn(event);

        mockMvc
            .perform(post("/api/schedule/{id}/event/{date}", id, "2022-10-01")
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.scheduleId", is(id.toString())))
            .andExpect(jsonPath("$.eventDate", is("2022-10-01T08:00:00")));
        verify(facade).completeEvent(userId, id, LocalDate.of(2022, 10, 1));
    }

    @Test
    void completeEvent_400() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.completeEvent(any(), any(), any())).thenThrow(new InvalidMedicationEventException("Validation failed"));

        mockMvc
            .perform(post("/api/schedule/{id}/event/{date}", id, "2022-10-01")
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void completeEvent_401() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc
            .perform(post("/api/schedule/{id}/event/{date}", id, "2022-10-01")
                .with(anonymous())
                .with(csrf()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void completeEvent_403() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(post("/api/schedule/{id}/event/{date}", id, "2022-10-01")
                .with(user(user)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }

    @Test
    void completeEvent_404() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.completeEvent(any(), any(), any())).thenThrow(new MedicationEventNotFoundException(LocalDate.of(2022, 10, 1)));

        mockMvc
            .perform(post("/api/schedule/{id}/event/{date}", id, "2022-10-01")
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("This medication should not be taken at 2022-10-01")));
    }
}