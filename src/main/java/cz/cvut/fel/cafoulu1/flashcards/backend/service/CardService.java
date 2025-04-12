package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardRequest;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing cards. It provides methods for creating, updating, deleting and getting cards.
 */
public interface CardService {
    /**
     * Creates a new card.
     *
     * @param cardSetId   the ID of the card set to which the card belongs
     * @param cardRequest the request object containing card details
     * @param userId      the ID of the user creating the card
     * @return the ID of the created card
     */
    UUID createCard(UUID cardSetId, CardRequest cardRequest, UUID userId);

    /**
     * Updates an existing card.
     *
     * @param cardSetId   the ID of the card set to which the card belongs
     * @param cardId      the ID of the card to be updated
     * @param cardRequest the request object containing updated card details
     * @param userId      the ID of the user updating the card
     */
    void updateCard(UUID cardSetId, UUID cardId, CardRequest cardRequest, UUID userId);

    /**
     * Retrieves a list of cards for a given card set.
     *
     * @param cardSetId the ID of the card set
     * @return a list of CardDto objects representing the cards in the card set
     */
    List<CardDto> getCards(UUID cardSetId);

    /**
     * Retrieves a specific card by its ID.
     *
     * @param cardSetId the ID of the card set to which the card belongs
     * @param cardId    the ID of the card to be deleted
     * @param userId    the ID of the user deleting the card
     */
    void deleteCard(UUID cardSetId, UUID cardId, UUID userId);
}
