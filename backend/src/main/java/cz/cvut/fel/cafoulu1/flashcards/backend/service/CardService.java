package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardRequest;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing cards. It provides methods for creating, updating, deleting and getting cards.
 */
public interface CardService {
    UUID createCard(UUID cardSetId, CardRequest cardRequest, UUID userId);

    void updateCard(UUID cardSetId, UUID cardId, CardRequest cardRequest, UUID userId);

    List<CardDto> getCards(UUID cardSetId);

    void deleteCard(UUID cardSetId, UUID cardId, UUID userId);
}
