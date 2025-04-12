package cz.cvut.fel.cafoulu1.flashcards.backend.repository;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Card;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for cards.
 */
public interface CardRepository extends JpaRepository<Card, UUID> {
    List<Card> findByCardSetOrderByCardOrderAsc(CardSet cardSet);
}