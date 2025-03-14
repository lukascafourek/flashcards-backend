package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicSetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.User}
 */
@Data
public class UserDto implements Serializable {
    UUID id;
    String email;
    String username;
    AuthProvider provider;
    List<BasicCardSetDto> cardSets;
    List<BasicSetStatisticsDto> setStatistics;
    List<BasicCardSetDto> favoriteSets; // probably not needed - may be removed
}
