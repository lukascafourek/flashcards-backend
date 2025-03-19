package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicSetStatisticsDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet}
 */
@Data
public class CardSetDto implements Serializable {
    BasicCardSetDto basicCardSetDto;
    BasicSetStatisticsDto setStatistics;
    List<CardDto> cards;
    Boolean favorite;
}
