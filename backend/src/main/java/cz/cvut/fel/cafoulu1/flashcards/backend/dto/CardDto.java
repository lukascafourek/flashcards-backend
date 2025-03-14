package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.Card}
 */
@Data
public class CardDto implements Serializable {
    UUID id;
    String front;
    String back;
    BasicCardSetDto cardSet;
}
