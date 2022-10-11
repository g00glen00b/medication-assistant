package be.g00glen00b.apps.mediminder.user.implementation;

import be.g00glen00b.apps.mediminder.user.UserAuthentication;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserAuthenticationTest {
    @Test
    void ofEntity_returnsResult() {
        UserEntity entity = UserEntity.of("me@example.org", "Atsushi Kristián", "password", ZoneId.of("Australia/Brisbane"));
        UserAuthentication result = UserAuthentication.ofEntity(entity);
        assertThat(result).isEqualTo(new UserAuthentication(
            entity.getId(),
            entity.getEmail(),
            entity.getPassword()
        ));
    }

    @Test
    void getAuthorities_containsUserRole() {
        UserAuthentication authentication = new UserAuthentication(UUID.randomUUID(), "me@example.org", "password");
        assertThat(authentication.getAuthorities())
            .extracting(GrantedAuthority::getAuthority)
            .contains("USER");
    }

    @Test
    void isAccountNonExpired_isAlwaysTrue() {
        UserAuthentication authentication = new UserAuthentication(UUID.randomUUID(), "me@example.org", "password");
        assertThat(authentication.isAccountNonExpired()).isTrue();
    }

    @Test
    void isAccountNonLocked_isAlwaysTrue() {
        UserAuthentication authentication = new UserAuthentication(UUID.randomUUID(), "me@example.org", "password");
        assertThat(authentication.isAccountNonLocked()).isTrue();
    }

    @Test
    void isCredentialsNonExpired_isAlwaysTrue() {
        UserAuthentication authentication = new UserAuthentication(UUID.randomUUID(), "me@example.org", "password");
        assertThat(authentication.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void isEnabled_isAlwaysTrue() {
        UserAuthentication authentication = new UserAuthentication(UUID.randomUUID(), "me@example.org", "password");
        assertThat(authentication.isEnabled()).isTrue();
    }
}