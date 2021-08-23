package be.g00glen00b.apps.medicationassistant.authentication;

import lombok.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

@Value
public class UserAuthenticationInfoDTO implements UserDetails {
    UUID id;
    String username;
    String password;
    List<SimpleGrantedAuthority> authorities;
    boolean enabled;

    public UserAuthenticationInfoDTO(User user) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority("USER"));
        this.enabled = true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
