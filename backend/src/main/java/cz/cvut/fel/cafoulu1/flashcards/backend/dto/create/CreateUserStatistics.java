package cz.cvut.fel.cafoulu1.flashcards.backend.dto.create;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import lombok.Value;

import java.io.Serializable;

@Value
public class CreateUserStatistics implements Serializable {
    BasicUserDto user;
    Integer totalSetsLearned;
    Integer totalCardsLearned;
    Integer totalCardsToLearnAgain;
    Integer setsCreated;
    Integer cardsCreated;
    Integer baseMethodModes;
    Integer multipleChoiceModes;
    Integer connectionModes;
}
