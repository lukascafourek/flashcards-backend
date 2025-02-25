package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.Card}
 */
@Value
public class CardDto implements Serializable {
    UUID id;
    String front;
    String back;
    BasicCardSetDto cardSet;
}
