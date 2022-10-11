package be.g00glen00b.apps.mediminder.user;

import be.g00glen00b.apps.mediminder.user.implementation.UserEntity;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Value
public class UserAuthentication implements UserDetails {
    UUID id;
    String username;
    String password;

    public static UserAuthentication ofEntity(UserEntity entity) {
        return new UserAuthentication(entity.getId(), entity.getEmail(), entity.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
