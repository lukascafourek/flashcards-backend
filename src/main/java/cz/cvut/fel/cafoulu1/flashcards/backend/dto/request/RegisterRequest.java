package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto that represents a request to register a new user.
 */
@Getter
@Setter
public class RegisterRequest {
    @Size(max = 255)
    private String email;
    @Size(min = 3, max = 255)
    private String username;
    @Size(min = 8, max = 255)
    private String password;
}
