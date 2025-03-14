package cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class BasicCardDto implements Serializable {
    UUID id;
    String front;
    String back;
}
