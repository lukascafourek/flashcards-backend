package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Category;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet}
 */
@Data
public class CardSetDto implements Serializable {
    UUID id;
    String name;
    String description;
    Category category;
    LocalDate creationDate;
    Boolean privacy;
    String creator;
}
