package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a register request.
 */
@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String username;
    private String password;
}
