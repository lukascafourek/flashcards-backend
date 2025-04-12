package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating or updating or creating a card.
 */
@Getter
@Setter
public class CardRequest {
    @Size(max = 510)
    private String front;
    @Size(max = 510)
    private String back;
    private String picture;
}
