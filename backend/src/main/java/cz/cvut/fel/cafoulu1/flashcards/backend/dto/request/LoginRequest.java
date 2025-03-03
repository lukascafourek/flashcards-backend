package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a login request.
 */
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
