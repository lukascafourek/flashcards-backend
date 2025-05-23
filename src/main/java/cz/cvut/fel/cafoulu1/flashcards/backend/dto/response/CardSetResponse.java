package cz.cvut.fel.cafoulu1.flashcards.backend.dto.response;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Category;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Response object for getting a card set.
 */
@Data
public class CardSetResponse implements Serializable {
    CardSetDto cardSetDto;
    Boolean favorite;
    Boolean creator;
    List<Category> categories;
}
