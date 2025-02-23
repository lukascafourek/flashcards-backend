package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics}
 */
@Value
public class SetStatisticsDto implements Serializable {
    UUID id;
    Integer setsLearned;
    Integer cardsLearned;
    Integer cardsToLearnAgain;
    Integer baseMethodMode;
    Integer multipleChoiceMode;
    Integer connectionMode;
    CardSetDto cardSet;
    UserDto user;
}
