package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.Token}
 */
@Data
public class TokenDto implements Serializable {
    UUID userId;
    BasicUserDto user;
    String resetToken;
    LocalDateTime expirationDate;
}
