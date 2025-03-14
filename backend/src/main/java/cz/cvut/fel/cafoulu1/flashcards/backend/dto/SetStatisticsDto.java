package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics}
 */
@Data
public class SetStatisticsDto implements Serializable {
    UUID id;
    Integer setsLearned;
    Integer cardsLearned;
    Integer cardsToLearnAgain;
    Integer baseMethodMode;
    Integer multipleChoiceMode;
    Integer connectionMode;
    BasicCardSetDto cardSet;
    BasicUserDto user;
}
