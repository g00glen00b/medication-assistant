package be.g00glen00b.apps.mediminder.user.implementation;

import be.g00glen00b.apps.mediminder.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final ZonedDateTime TODAY = ZonedDateTime.of(2022, 10, 7, 10, 0, 0, 0, ZoneId.of("UTC"));
    private UserService service;
    @Mock
    private UserEntityRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Captor
    private ArgumentCaptor<UserEntity> anyEntity;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(TODAY.toInstant(), TODAY.getZone());
        service = new UserService(repository, passwordEncoder, fixedClock);
    }

    @Test
    void create_returnsResult() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        CreateUserRequestDTO request = new CreateUserRequestDTO("me@example.org", "Driskoll Virginie", "password", zone);
        when(passwordEncoder.encode(any())).thenAnswer(prefixFirstArgument("encoded-"));
        when(repository.save(any())).thenAnswer(returnsFirstArg());
        when(repository.existsByEmail(any())).thenReturn(false);

        UserInfoDTO result = service.create(request);
        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new UserInfoDTO(null, "me@example.org", "Driskoll Virginie", zone));
        assertThat(result.id()).isNotNull();
    }

    @Test
    void create_savesEntity() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        CreateUserRequestDTO request = new CreateUserRequestDTO("me@example.org", "Driskoll Virginie", "password", zone);
        when(passwordEncoder.encode(any())).thenAnswer(prefixFirstArgument("encoded-"));
        when(repository.save(any())).thenAnswer(returnsFirstArg());
        when(repository.existsByEmail(any())).thenReturn(false);

        service.create(request);
        verify(repository).save(anyEntity.capture());
        assertThat(anyEntity.getValue())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(UserEntity.of("me@example.org", "Driskoll Virginie", "encoded-password", zone));
        assertThat(anyEntity.getValue().getId()).isNotNull();
    }

    @Test
    void create_encodesPassword() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        CreateUserRequestDTO request = new CreateUserRequestDTO("me@example.org", "Driskoll Virginie", "password", zone);
        when(passwordEncoder.encode(any())).thenAnswer(prefixFirstArgument("encoded-"));
        when(repository.save(any())).thenAnswer(returnsFirstArg());
        when(repository.existsByEmail(any())).thenReturn(false);

        service.create(request);
        verify(passwordEncoder).encode("password");
    }

    @Test
    void create_throwsExceptionIfEmailAlreadyUsed() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        CreateUserRequestDTO request = new CreateUserRequestDTO("me@example.org", "Driskoll Virginie", "password", zone);
        when(repository.existsByEmail(any())).thenReturn(true);

        assertThatExceptionOfType(InvalidUserException.class)
            .isThrownBy(() -> service.create(request))
            .withMessage("There is already a user with this e-mail address");
        verify(repository).existsByEmail("me@example.org");
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void findById_returnsResult() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        UserEntity entity = UserEntity.of("me@example.org", "Driskoll Virginie", "password", zone);
        when(repository.findById(any())).thenReturn(Optional.of(entity));

        UserInfoDTO result = service.findById(entity.getId());
        assertThat(result).isEqualTo(UserInfoDTO.ofEntity(entity));
    }

    @Test
    void findById_usesRepository() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        UserEntity entity = UserEntity.of("me@example.org", "Driskoll Virginie", "password", zone);
        when(repository.findById(any())).thenReturn(Optional.of(entity));

        service.findById(entity.getId());
        verify(repository).findById(entity.getId());
    }

    @Test
    void update_returnsResult() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        UserEntity entity = UserEntity.of("me@example.org", "Driskoll Virginie", "password", zone);
        UpdateUserRequestDTO request = new UpdateUserRequestDTO("Adam Bran", ZoneId.of("UTC"));
        when(repository.findById(any())).thenReturn(Optional.of(entity));

        UserInfoDTO result = service.update(entity.getId(), request);
        assertThat(result).isEqualTo(new UserInfoDTO(
            entity.getId(),
            entity.getEmail(),
            request.name(),
            request.timezone()
        ));
    }

    @Test
    void update_usesRepository() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        UserEntity entity = UserEntity.of("me@example.org", "Driskoll Virginie", "password", zone);
        UpdateUserRequestDTO request = new UpdateUserRequestDTO("Adam Bran", ZoneId.of("UTC"));
        when(repository.findById(any())).thenReturn(Optional.of(entity));

        service.update(entity.getId(), request);
        verify(repository).findById(entity.getId());
    }

    @Test
    void update_throwsExceptionIfUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        UpdateUserRequestDTO request = new UpdateUserRequestDTO("Adam Bran", ZoneId.of("UTC"));

        assertThatExceptionOfType(UserNotFoundException.class)
            .isThrownBy(() -> service.update(id, request))
            .withMessage("User with ID '" + id + "' was not found");
    }

    @Test
    void updateCredentials_returnsResult() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        UserEntity entity = UserEntity.of("me@example.org", "Driskoll Virginie", "password", zone);
        UpdateCredentialsRequestDTO request = new UpdateCredentialsRequestDTO("password", "newPassword");
        when(repository.findById(any())).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        UserInfoDTO result = service.updateCredentials(entity.getId(), request);
        assertThat(result).isEqualTo(UserInfoDTO.ofEntity(entity));
    }

    @Test
    void updateCredentials_usesRepository() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        UserEntity entity = UserEntity.of("me@example.org", "Driskoll Virginie", "password", zone);
        UpdateCredentialsRequestDTO request = new UpdateCredentialsRequestDTO("password", "newPassword");
        when(repository.findById(any())).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        service.updateCredentials(entity.getId(), request);
        verify(repository).findById(entity.getId());
    }

    @Test
    void updateCredentials_throwsExceptionIfPaswordDoesntMatch() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        UserEntity entity = UserEntity.of("me@example.org", "Driskoll Virginie", "encoded-password", zone);
        UpdateCredentialsRequestDTO request = new UpdateCredentialsRequestDTO("password", "newPassword");
        when(repository.findById(any())).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThatExceptionOfType(InvalidUserException.class)
            .isThrownBy(() -> service.updateCredentials(entity.getId(), request))
            .withMessage("The password is not correct");
        verify(passwordEncoder).matches("password", "encoded-password");
    }

    @Test
    void updateCredentials_throwsExceptionIfUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        UpdateCredentialsRequestDTO request = new UpdateCredentialsRequestDTO("password", "newPassword");

        assertThatExceptionOfType(UserNotFoundException.class)
            .isThrownBy(() -> service.updateCredentials(id, request))
            .withMessage("User with ID '" + id + "' was not found");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void existsById_returnsResult() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(any())).thenReturn(true);
        assertThat(service.existsById(id)).isTrue();
        verify(repository).existsById(id);
    }

    @Test
    void existsByEmail_returnsResult() {
        when(repository.existsByEmail(any())).thenReturn(true);
        assertThat(service.existsByEmail("me@example.org")).isTrue();
        verify(repository).existsByEmail("me@example.org");
    }

    @Test
    void loadByUsername_returnsResult() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        UserEntity entity = UserEntity.of("me@example.org", "Driskoll Virginie", "encoded-password", zone);
        when(repository.findByEmail(any())).thenReturn(Optional.of(entity));

        UserDetails result = service.loadUserByUsername("me@example.org");
        assertThat(result).isEqualTo(UserAuthentication.ofEntity(entity));
    }

    @Test
    void loadByUsername_usesRepository() {
        ZoneId zone = ZoneId.of("Australia/Brisbane");
        UserEntity entity = UserEntity.of("me@example.org", "Driskoll Virginie", "encoded-password", zone);
        when(repository.findByEmail(any())).thenReturn(Optional.of(entity));

        service.loadUserByUsername("me@example.org");
        verify(repository).findByEmail("me@example.org");
    }

    @Test
    void loadByUsername_throwsExceptionIfEntityNotFound() {
        assertThatExceptionOfType(UsernameNotFoundException.class)
            .isThrownBy(() -> service.loadUserByUsername("me@example.org"))
            .withMessage("There is no such user");
    }

    @Test
    void findAvailableTimezones_returnsAllAvailableZoneIds() {
        assertThat(service.findAvailableTimezones()).containsAll(ZoneId.getAvailableZoneIds());
    }

    private Answer<String> prefixFirstArgument(String prefix) {
        return invocation -> prefix + invocation.getArgument(0, String.class);
    }
}