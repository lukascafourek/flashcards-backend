package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics}
 */
@Value
public class UserStatisticsDto implements Serializable {
    UUID userId;
    UserDto user;
    Integer totalSetsLearned;
    Integer totalCardsLearned;
    Integer totalCardsToLearnAgain;
    Integer setsCreated;
    Integer cardsCreated;
    Integer baseMethodModes;
    Integer multipleChoiceModes;
    Integer connectionModes;
}
