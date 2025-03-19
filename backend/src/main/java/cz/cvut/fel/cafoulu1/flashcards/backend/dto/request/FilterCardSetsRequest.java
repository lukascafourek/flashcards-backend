package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FilterCardSetsRequest {
    private UUID userId;
    private Boolean favorite;
    private String category;
    private String search;
}
