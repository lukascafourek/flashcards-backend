package cz.cvut.fel.cafoulu1.flashcards.backend.dto.create;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicSetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class CreateUser implements Serializable {
    String email;
    String password;
    String username;
    AuthProvider provider;
    List<BasicCardSetDto> cardSets;
    List<BasicSetStatisticsDto> setStatistics;
    List<BasicCardSetDto> favoriteSets; // probably not needed - may be removed
}
