package cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class BasicTokenDto implements Serializable {
    UUID userId;
    String resetToken;
    LocalDateTime expirationDate;
}
