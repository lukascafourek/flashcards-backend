package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicSetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Category;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet}
 */
@Data
public class CardSetDto implements Serializable {
    UUID id;
    String name;
    Category category;
    LocalDate creationDate;
    BasicUserDto user;
    List<BasicCardDto> cards;
    List<BasicSetStatisticsDto> setStatistics;
    List<BasicUserDto> favoriteUsers;   // probably not needed - may be removed
}
