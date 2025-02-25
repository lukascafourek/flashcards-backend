package cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class BasicCardDto implements Serializable {
    UUID id;
    String front;
    String back;
}
