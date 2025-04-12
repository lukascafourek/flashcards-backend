package cz.cvut.fel.cafoulu1.flashcards.backend.repository;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for user statistics.
 */
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, UUID> {
}