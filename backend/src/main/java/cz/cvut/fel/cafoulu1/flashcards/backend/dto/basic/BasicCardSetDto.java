package cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Category;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class BasicCardSetDto implements Serializable {
    UUID id;
    String name;
    Category category;
    LocalDate creationDate;
}
