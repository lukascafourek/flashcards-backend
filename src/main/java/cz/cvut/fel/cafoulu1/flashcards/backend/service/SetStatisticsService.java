package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import java.util.UUID;

/**
 * Set statistics service for handling set statistics requests. It is used for incrementing wanted set statistics.
 */
public interface SetStatisticsService {
    /**
     * Increments the wanted statistics for a card set.
     *
     * @param cardSetId     the ID of the card set
     * @param userId        the ID of the user
     * @param firstStat     the first statistic to increment
     * @param secondStat    the second statistic to increment (optional)
     */
    void incrementWantedStatistics(UUID cardSetId, UUID userId, String firstStat, String secondStat);
}
