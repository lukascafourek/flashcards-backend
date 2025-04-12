package cz.cvut.fel.cafoulu1.flashcards.backend.dto.response;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for getting full card set information.
 */
@Getter
@Setter
public class FullCardSetInfo {
    private UUID id;
    private String name;
    private Category category;
    private LocalDate creationDate;
    private UUID userId;
    private String description;
    private Boolean privacy;
}
