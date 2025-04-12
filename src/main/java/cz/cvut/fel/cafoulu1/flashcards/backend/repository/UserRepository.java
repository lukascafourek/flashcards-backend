package cz.cvut.fel.cafoulu1.flashcards.backend.repository;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for users.
 */
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}