package cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class BasicSetStatisticsDto implements Serializable {
    UUID id;
    Integer setsLearned;
    Integer cardsLearned;
    Integer cardsToLearnAgain;
    Integer baseMethodMode;
    Integer multipleChoiceMode;
    Integer connectionMode;
}
