package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserStatisticsDto implements Serializable {
    Integer totalSetsLearned;
    Integer totalCardsLearned;
    Integer totalCardsToLearnAgain;
    Integer setsCreated;
    Integer cardsCreated;
    Integer baseMethodModes;
    Integer multipleChoiceModes;
    Integer trueFalseModes;
}
