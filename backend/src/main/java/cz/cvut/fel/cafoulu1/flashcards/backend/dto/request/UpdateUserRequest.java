package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String username;
    private String email;
    private String password;
    private String check;
}
