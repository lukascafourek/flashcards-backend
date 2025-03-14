package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardDto;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.Picture}
 */
@Data
public class PictureDto implements Serializable {
    UUID cardId;
    BasicCardDto card;
    byte[] picture;
}
