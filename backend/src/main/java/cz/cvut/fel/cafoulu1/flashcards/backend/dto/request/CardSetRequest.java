package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardSetRequest {
    private String name;
    private String category;
    private Boolean favorite;
}
