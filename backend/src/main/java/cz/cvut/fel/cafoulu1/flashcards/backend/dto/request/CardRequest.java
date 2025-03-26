package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequest {
    private String front;
    private String back;
    private String picture;
}
