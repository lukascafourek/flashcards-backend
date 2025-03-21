package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardSetRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.CardSetsResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service for managing card sets. It provides methods for creating, updating, deleting and getting card sets.
 */
public interface CardSetService {
    void createCardSet(UUID userId, CardSetRequest cardSetRequest);

    void updateCardSet(UUID cardSetId, CardSetRequest cardSetRequest);

    CardSetDto getCardSet(UUID cardSetId);

    CardSetsResponse getFilteredCardSets(Pageable pageable, FilterCardSetsRequest filterCardSetsRequest);

    void deleteCardSet(UUID cardSetId);
}
