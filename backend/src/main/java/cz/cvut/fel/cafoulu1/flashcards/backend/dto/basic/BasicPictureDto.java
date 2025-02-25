package cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class BasicPictureDto implements Serializable {
    UUID cardId;
    byte[] picture;
}
