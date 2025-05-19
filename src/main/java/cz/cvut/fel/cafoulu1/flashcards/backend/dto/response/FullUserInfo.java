package cz.cvut.fel.cafoulu1.flashcards.backend.dto.response;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Dto for getting full user information.
 */
@Getter
@Setter
public class FullUserInfo {
    UUID id;
    String email;
    String username;
    AuthProvider provider;
    Role role;
    Integer numberOfImages;
}
