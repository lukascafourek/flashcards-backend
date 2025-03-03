package cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.UUID;

/**
 * This code was taken from <a href="https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-core/src/main/java/com/baeldung/jwtsignkey/userservice/UserDetailsImpl.java">eugenp GitHub user</a>
 * and modified for the purpose of this application.
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

    public UserDetailsImpl(UUID id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
