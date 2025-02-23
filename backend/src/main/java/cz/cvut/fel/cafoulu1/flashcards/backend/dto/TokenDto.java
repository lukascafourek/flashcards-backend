package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.Token}
 */
@Value
public class TokenDto implements Serializable {
    UUID userId;
    UserDto user;
    String resetToken;
    LocalDateTime expirationDate;
}
