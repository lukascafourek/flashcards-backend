package cz.cvut.fel.cafoulu1.flashcards.backend.dto.create;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import lombok.Value;

import java.io.Serializable;

@Value
public class CreateCard implements Serializable {
    String front;
    String back;
    BasicCardSetDto cardSetDto;
}
