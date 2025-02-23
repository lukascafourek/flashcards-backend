package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Category;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet}
 */
@Value
public class CardSetDto implements Serializable {
    UUID id;
    String name;
    Category category;
    LocalDate creationDate;
    UserDto user;
    List<CardDto> cards;
    List<SetStatisticsDto> setStatistics;
    List<UserDto> favoriteUsers;
}
