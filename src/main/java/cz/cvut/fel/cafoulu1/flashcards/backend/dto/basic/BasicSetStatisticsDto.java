package cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class BasicSetStatisticsDto implements Serializable {
    UUID id;
    Integer setsLearned;
    Integer cardsLearned;
    Integer cardsToLearnAgain;
    Integer baseMethodMode;
    Integer multipleChoiceMode;
    Integer trueFalseMode;
}
