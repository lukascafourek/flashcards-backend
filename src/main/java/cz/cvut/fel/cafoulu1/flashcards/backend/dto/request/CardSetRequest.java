package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating or updating or creating a card set.
 */
@Getter
@Setter
public class CardSetRequest {
    @Size(max = 255)
    private String name;
    @Size(max = 255)
    private String description;
    @Size(max = 255)
    private String category;
    private Boolean privacy;
}
