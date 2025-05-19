package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SetStatisticsDto implements Serializable {
    Integer setsLearned;
    Integer cardsLearned;
    Integer cardsToLearnAgain;
    Integer baseMethodMode;
    Integer multipleChoiceMode;
    Integer trueFalseMode;
}
