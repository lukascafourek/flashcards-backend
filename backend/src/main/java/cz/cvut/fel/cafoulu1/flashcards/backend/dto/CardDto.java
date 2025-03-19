package cz.cvut.fel.cafoulu1.flashcards.backend.dto;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicPictureDto;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link cz.cvut.fel.cafoulu1.flashcards.backend.model.Card}
 */
@Data
public class CardDto implements Serializable {
    BasicCardDto card;
    BasicPictureDto picture;
}
