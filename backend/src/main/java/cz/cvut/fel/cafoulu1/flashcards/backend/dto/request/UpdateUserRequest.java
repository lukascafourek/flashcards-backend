package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO Request object for updating user information.
 */
@Getter
@Setter
public class UpdateUserRequest {
    @Size(min = 3, max = 255)
    private String username;
    @Size(max = 255)
    private String email;
    @Size(min = 8, max = 255)
    private String password;
    @Size(min = 8, max = 255)
    private String check;
}
