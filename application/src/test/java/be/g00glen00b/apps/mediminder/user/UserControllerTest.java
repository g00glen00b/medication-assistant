package be.g00glen00b.apps.mediminder.user;

import be.g00glen00b.apps.mediminder.user.configuration.WebSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Import(WebSecurityConfiguration.class)

class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserFacade facade;

    @Test
    void create_200() throws Exception {
        UUID id = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(id, "me@example.org", "Brittany Jackson", ZoneId.of("Australia/Brisbane"));
        when(facade.create(any())).thenReturn(user);

        mockMvc
            .perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "email": "me@example.org",
                    "name": "Britanny Jackson",
                    "password": "password",
                    "timezone": "Australia/Brisbane"
                }
                """)
                .with(anonymous())
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())));
        verify(facade).create(new CreateUserRequestDTO("me@example.org", "Britanny Jackson", "password", ZoneId.of("Australia/Brisbane")));
    }

    @Test
    void create_400() throws Exception {
        UUID id = UUID.randomUUID();
        when(facade.create(any())).thenThrow(new InvalidUserException("Validation failed"));

        mockMvc
            .perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "email": "me@example.org",
                    "name": "Britanny Jackson",
                    "password": "password",
                    "timezone": "Australia/Brisbane"
                }
                """)
                .with(anonymous())
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void update_200() throws Exception {
        UUID id = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(id, "me@example.org", "Brittany Jackson", ZoneId.of("Australia/Brisbane"));
        UserAuthentication authentication = new UserAuthentication(id, "me@example.org", "password");
        when(facade.update(any(), any())).thenReturn(user);

        mockMvc
            .perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "name": "Britanny Jackson",
                    "timezone": "Australia/Brisbane"
                }
                """)
                .with(user(authentication))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())));
        verify(facade).update(id, new UpdateUserRequestDTO("Britanny Jackson", ZoneId.of("Australia/Brisbane")));
    }

    @Test
    void update_400() throws Exception {
        UUID id = UUID.randomUUID();
        UserAuthentication authentication = new UserAuthentication(id, "me@example.org", "password");
        when(facade.update(any(), any())).thenThrow(new InvalidUserException("Validation failed"));

        mockMvc
            .perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "name": "Britanny Jackson",
                    "timezone": "Australia/Brisbane"
                }
                """)
                .with(user(authentication))
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void update_401() throws Exception {
        mockMvc
            .perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "name": "Britanny Jackson",
                    "timezone": "Australia/Brisbane"
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
        UserAuthentication authentication = new UserAuthentication(id, "me@example.org", "password");

        mockMvc
            .perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "name": "Britanny Jackson",
                    "timezone": "Australia/Brisbane"
                }
                """)
                .with(user(authentication)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }

    @Test
    void updateCredentials_200() throws Exception {
        UUID id = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(id, "me@example.org", "Brittany Jackson", ZoneId.of("Australia/Brisbane"));
        UserAuthentication authentication = new UserAuthentication(id, "me@example.org", "password");
        when(facade.updateCredentials(any(), any())).thenReturn(user);

        mockMvc
            .perform(put("/api/user/credentials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "oldPassword": "password",
                    "newPassword": "password2"
                }
                """)
                .with(user(authentication))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())));
        verify(facade).updateCredentials(id, new UpdateCredentialsRequestDTO("password", "password2"));
    }

    @Test
    void updateCredentials_400() throws Exception {
        UUID id = UUID.randomUUID();
        UserAuthentication authentication = new UserAuthentication(id, "me@example.org", "password");
        when(facade.updateCredentials(any(), any())).thenThrow(new InvalidUserException("Validation failed"));

        mockMvc
            .perform(put("/api/user/credentials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "oldPassword": "password",
                    "newPassword": "password2"
                }
                """)
                .with(user(authentication))
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void updateCredentials_401() throws Exception {
        mockMvc
            .perform(put("/api/user/credentials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "oldPassword": "password",
                    "newPassword": "password2"
                }
                """)
                .with(anonymous())
                .with(csrf()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void updateCredentials_403() throws Exception {
        UUID id = UUID.randomUUID();
        UserAuthentication authentication = new UserAuthentication(id, "me@example.org", "password");

        mockMvc
            .perform(put("/api/user/credentials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "oldPassword": "password",
                    "newPassword": "password2"
                }
                """)
                .with(user(authentication)))
            .andExpect(status().isForbidden());
        verifyNoInteractions(facade);
    }

    @Test
    void findAllTimezones_200() throws Exception {
        when(facade.findAvailableTimezones()).thenReturn(List.of("UTC"));

        mockMvc
            .perform(get("/api/user/timezone"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]", is("UTC")));
        verify(facade).findAvailableTimezones();
    }

    @Test
    void existsUser_200() throws Exception {
        when(facade.existsByEmail(anyString())).thenReturn(true);

        mockMvc
            .perform(get("/api/user/email/exists")
                .param("email", "me@example.org"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
        verify(facade).existsByEmail("me@example.org");
    }

    @Test
    void findCurrentUser_200() throws Exception {
        UUID id = UUID.randomUUID();
        UserInfoDTO user = new UserInfoDTO(id, "me@example.org", "Brittany Jackson", ZoneId.of("Australia/Brisbane"));
        UserAuthentication authentication = new UserAuthentication(id, "me@example.org", "password");
        when(facade.findById(any())).thenReturn(user);

        mockMvc
            .perform(get("/api/user/current")
                .with(user(authentication))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.toString())));
        verify(facade).findById(id);
    }

    @Test
    void findCurrentUser_401() throws Exception {
        mockMvc
            .perform(get("/api/user/current")
                .with(csrf()))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(facade);
    }

    @Test
    void findCurrentUser_404() throws Exception {
        UUID id = UUID.randomUUID();
        UserAuthentication authentication = new UserAuthentication(id, "me@example.org", "password");
        when(facade.findById(any())).thenThrow(new UserNotFoundException(id));

        mockMvc
            .perform(get("/api/user/current")
                .with(user(authentication))
                .with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("User with ID '" + id + "' was not found")));
    }
}