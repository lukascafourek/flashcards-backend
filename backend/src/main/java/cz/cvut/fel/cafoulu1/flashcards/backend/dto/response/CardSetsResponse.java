package cz.cvut.fel.cafoulu1.flashcards.backend.dto.response;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class CardSetsResponse {
    Integer pages;
//    Integer setsCountOnPage;
    List<Category> categories;
    List<BasicCardSetDto> cardSets;
}
