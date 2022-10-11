package be.g00glen00b.apps.mediminder.notification;

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
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
@Import(WebSecurityConfiguration.class)
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NotificationFacade facade;

    @Test
    void findAll_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        NotificationDTO notification = new NotificationDTO(id, "Message", NotificationType.INFO, Instant.now());
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<NotificationDTO> page = new PageImpl<>(List.of(notification), pageRequest, 1);
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        when(facade.findAll(any(), any())).thenReturn(page);

        mockMvc
            .perform(get("/api/notification")
                .param("page", "0")
                .param("size", "10")
                .with(user(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(id.toString())));
        verify(facade).findAll(userId, pageRequest);
    }

    @Test
    void findAll_401() throws Exception {
        mockMvc
            .perform(get("/api/notification")
                .param("page", "0")
                .param("size", "10")
                .with(anonymous()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void delete_200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");

        mockMvc
            .perform(delete("/api/notification/{id}", id)
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isOk());
        verify(facade).delete(userId, id);
    }

    @Test
    void delete_401() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc
            .perform(delete("/api/notification/{id}", id)
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
            .perform(delete("/api/notification/{id}", id)
                .with(user(user)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }

    @Test
    void delete_404() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserAuthentication user = new UserAuthentication(userId, "me@example.org", "password");
        doThrow(new NotificationNotFoundException(id)).when(facade).delete(any(), any());

        mockMvc
            .perform(delete("/api/notification/{id}", id)
                .with(user(user))
                .with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Notification with ID '" + id + "' was not found")));
    }
}