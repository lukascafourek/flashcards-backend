package cz.cvut.fel.cafoulu1.flashcards.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO for getting full card information.
 */
@Getter
@Setter
public class FullCardInfo {
    private UUID id;
    private String front;
    private String back;
    private String picture;
    private String mimeType;
    private UUID cardSetId;
}
