package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics}
 */
@Data
public class UserStatisticsDto implements Serializable {
    UUID userId;
    BasicUserDto user;
    Integer totalSetsLearned;
    Integer totalCardsLearned;
    Integer totalCardsToLearnAgain;
    Integer setsCreated;
    Integer cardsCreated;
    Integer baseMethodModes;
    Integer multipleChoiceModes;
    Integer trueFalseModes;
}
