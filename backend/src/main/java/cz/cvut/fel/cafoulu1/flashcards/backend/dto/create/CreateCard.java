package cz.cvut.fel.cafoulu1.flashcards.backend.dto.create;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateCard implements Serializable {
    String front;
    String back;
    BasicCardSetDto cardSetDto;
}
