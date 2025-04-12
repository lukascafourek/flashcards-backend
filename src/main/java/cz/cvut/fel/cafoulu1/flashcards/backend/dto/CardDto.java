package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.Card}
 */
@Data
public class CardDto implements Serializable {
    private UUID id;
    private String front;
    private String back;
    private String picture;
    private String mimeType;
}
