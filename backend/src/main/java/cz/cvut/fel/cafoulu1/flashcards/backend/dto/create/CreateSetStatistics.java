package cz.cvut.fel.cafoulu1.flashcards.backend.dto.create;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateSetStatistics implements Serializable {
    Integer setsLearned;
    Integer cardsLearned;
    Integer cardsToLearnAgain;
    Integer baseMethodMode;
    Integer multipleChoiceMode;
    Integer trueFalseMode;
    BasicCardSetDto cardSet;
    BasicUserDto user;
}
