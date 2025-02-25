package cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class BasicUserDto implements Serializable {
    UUID id;
    String email;
    String username;
    AuthProvider provider;
}
