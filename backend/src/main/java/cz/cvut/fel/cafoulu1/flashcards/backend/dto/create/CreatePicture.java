package cz.cvut.fel.cafoulu1.flashcards.backend.dto.create;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreatePicture implements Serializable {
    BasicCardDto card;
    byte[] picture;
}
