package cz.cvut.fel.cafoulu1.flashcards.backend.service.oauth2;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class represents a custom OAuth2 user.
 */
public class OAuth2UserImpl implements OAuth2User {
    private final UserDto user;
    private final Map<String, Object> attributes;

    public OAuth2UserImpl(UserDto user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    public String getEmail() {
        return user.getEmail();
    }
}
