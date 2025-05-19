package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.User}
 */
@Data
public class UserDto implements Serializable {
    String email;
    String username;
    AuthProvider provider;
}
