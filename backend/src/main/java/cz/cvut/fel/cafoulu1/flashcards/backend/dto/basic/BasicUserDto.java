package cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class BasicUserDto implements Serializable {
    String email;
    String username;
    AuthProvider provider;
}
