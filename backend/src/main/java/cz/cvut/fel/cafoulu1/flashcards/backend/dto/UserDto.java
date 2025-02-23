package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.User}
 */
@Value
public class UserDto implements Serializable {
    UUID id;
    String email;
    String password;
    String username;
    List<CardSetDto> cardSets;
    List<SetStatisticsDto> setStatistics;
    List<CardSetDto> favoriteSets;
}
