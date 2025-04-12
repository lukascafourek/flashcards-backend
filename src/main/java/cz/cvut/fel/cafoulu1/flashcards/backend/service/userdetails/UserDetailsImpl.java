package cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * This code was taken from
 * <a href="https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-core/src/main/java/com/baeldung/jwtsignkey/userservice/UserDetailsImpl.java">eugenp</a>
 * on GitHub and modified for the purpose of this application.
 * <p>
 * This class implements the UserDetails interface and is used by Spring Security to represent the user details
 */
public class UserDetailsImpl implements UserDetails {
    @Serial
    @Getter
    private static final long serialVersionUID = 1L;

    @Getter
    private final UUID id;

    private final String email;

    @JsonIgnore
    private final String password;

    private final List<GrantedAuthority> authorities;

    public UserDetailsImpl(UUID id, String email, String password, List<GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl) o;
        return id.equals(user.id);
    }
}
