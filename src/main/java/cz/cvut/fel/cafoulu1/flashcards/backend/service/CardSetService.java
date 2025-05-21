package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.CardSetResponse;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardSetRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardSetInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.CardSetsResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing card sets. It provides methods for creating, updating, deleting and getting card sets.
 */
public interface CardSetService {
    /**
     * Creates a new card set.
     *
     * @param userId            the ID of the user creating the card set
     * @param cardSetRequest    the request containing the details of the card set to be created
     * @return the ID of the created card set
     */
    UUID createCardSet(UUID userId, CardSetRequest cardSetRequest);

    /**
     * Copies an existing card set.
     *
     * @param cardSetId the ID of the card set to be copied
     * @param userId    the ID of the user copying the card set
     * @return the ID of the copied card set
     */
    UUID copyCardSet(UUID cardSetId, UUID userId);

    /**
     * Updates an existing card set.
     *
     * @param cardSetId         the ID of the card set to be updated
     * @param cardSetRequest    the request containing the updated details of the card set
     * @param userId            the ID of the user updating the card set
     */
    void updateCardSet(UUID cardSetId, CardSetRequest cardSetRequest, UUID userId);

    /**
     * Updates the favorite status of a card set.
     *
     * @param cardSetId     the ID of the card set to be updated
     * @param isFavorite    the new favorite status
     * @param userId        the ID of the user updating the card set
     */
    void updateFavoriteCardSet(UUID cardSetId, Boolean isFavorite, UUID userId);

    /**
     * Gets the details of a card set.
     *
     * @param cardSetId the ID of the card set to be retrieved
     * @param userId    the ID of the user requesting the card sets
     * @return the details of the card set
     */
    CardSetResponse getCardSet(UUID cardSetId, UUID userId);

    /**
     * Gets a paginated list of card sets.
     *
     * @param pageable              the pagination information
     * @param filterCardSetsRequest the request containing the filters for the card sets
     * @return a paginated list of card sets
     */
    CardSetsResponse getFilteredCardSets(Pageable pageable, FilterCardSetsRequest filterCardSetsRequest);

    /**
     * Gets a list of all card sets.
     *
     * @return a list of all card sets
     */
    List<FullCardSetInfo> getAllCardSets();

    /**
     * Gets a list of all cards.
     *
     * @return a list of all cards
     */
    List<FullCardInfo> getAllCards();

    /**
     * Gets a list of all cards in a card set.
     *
     * @param cardSetId the ID of the card set to be deleted
     * @param userId    the ID of the user requesting the cards
     */
    void deleteCardSet(UUID cardSetId, UUID userId);

    /**
     * Updates the order of cards in a card set.
     *
     * @param cardSetId the ID of the card set
     * @param cardDtos  the list of cards with updated order
     * @param userId    the ID of the user updating the order
     */
    void updateOrderOfCards(UUID cardSetId, List<CardDto> cardDtos, UUID userId);
}
