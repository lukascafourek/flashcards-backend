package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequest {
    @Size(max = 510)
    private String front;
    @Size(max = 510)
    private String back;
    private String picture;
}
