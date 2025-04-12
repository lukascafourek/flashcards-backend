package cz.cvut.fel.cafoulu1.flashcards.backend.repository;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for tokens.
 */
public interface TokenRepository extends JpaRepository<Token, UUID> {
    void deleteAllByExpirationDateBefore(LocalDateTime now);
}