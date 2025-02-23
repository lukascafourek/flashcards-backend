package cz.cvut.fel.cafoulu1.flashcards.backend.repository;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for cards.
 */
public interface CardRepository extends JpaRepository<Card, UUID> {
}