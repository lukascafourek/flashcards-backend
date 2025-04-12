package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class that represents the data required for a login request.
 */
@Getter
@Setter
public class LoginRequest {
    @Size(max = 255)
    private String email;
    @Size(max = 255)
    private String password;
}
