package cz.cvut.fel.cafoulu1.flashcards.backend.repository;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for set statistics.
 */
public interface SetStatisticsRepository extends JpaRepository<SetStatistics, UUID> {
    void deleteByUserId(UUID userId);

    void deleteByCardSetId(UUID cardSetId);

    Optional<SetStatistics> findByCardSetIdAndUserId(UUID cardSetId, UUID userId);
}