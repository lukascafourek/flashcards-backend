package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.SetStatisticsDto;

import java.util.UUID;

/**
 * Service for managing set statistics. It provides a method for getting set statistics.
 */
public interface SetStatisticsService {
    /**
     * Gets the set statistics for a given user and card set.
     *
     * @param userId     the ID of the user
     * @param cardSetId  the ID of the card set
     * @return the set statistics details
     */
    SetStatisticsDto getSetStatistics(UUID userId, UUID cardSetId);
}
