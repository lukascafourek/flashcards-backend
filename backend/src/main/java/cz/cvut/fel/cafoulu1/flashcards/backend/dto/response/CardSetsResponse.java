package cz.cvut.fel.cafoulu1.flashcards.backend.dto.response;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Response object for getting paginated card sets.
 */
@Getter
@Setter
public class CardSetsResponse {
    Integer pages;
    List<Category> categories;
    List<BasicCardSetDto> cardSets;
}
