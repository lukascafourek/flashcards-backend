package cz.cvut.fel.cafoulu1.flashcards.backend.dto.create;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicSetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Category;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateCardSet implements Serializable {
    String name;
    Category category;
    LocalDate creationDate;
    BasicUserDto user;
    List<BasicCardDto> cards;
    List<BasicSetStatisticsDto> setStatistics;
    List<BasicUserDto> favoriteUsers;   // probably not needed - may be removed
}
