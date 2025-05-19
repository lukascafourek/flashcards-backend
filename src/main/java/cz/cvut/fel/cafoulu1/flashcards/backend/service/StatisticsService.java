package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import java.util.UUID;

/**
 * Statistics service for handling statistics requests. It is used for incrementing wanted statistics.
 */
public interface StatisticsService {
    /**
     * Increments the wanted statistics.
     *
     * @param cardSetId     the ID of the card set
     * @param userId        the ID of the user
     * @param firstStat     the first statistic to increment
     * @param secondStat    the second statistic to increment (optional)
     */
    void incrementWantedStatistics(UUID cardSetId, UUID userId, String firstStat, String secondStat);
}
