package cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class BasicUserStatisticsDto implements Serializable {
    UUID userId;
    Integer totalSetsLearned;
    Integer totalCardsLearned;
    Integer totalCardsToLearnAgain;
    Integer setsCreated;
    Integer cardsCreated;
    Integer baseMethodModes;
    Integer multipleChoiceModes;
    Integer connectionModes;
}
