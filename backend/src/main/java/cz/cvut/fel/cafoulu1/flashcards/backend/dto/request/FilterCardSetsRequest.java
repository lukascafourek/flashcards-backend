package cz.cvut.fel.cafoulu1.flashcards.backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FilterCardSetsRequest {
    private UUID userId;
    private Boolean mine;
    private Boolean favorite;
    @Size(max = 255)
    private String category;
    @Size(max = 255)
    private String search;
}
