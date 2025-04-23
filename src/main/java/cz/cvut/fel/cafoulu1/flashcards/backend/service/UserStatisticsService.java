package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserStatisticsDto;

import java.util.UUID;

/**
 * Service for managing user statistics. It provides method for getting user statistics.
 */
public interface UserStatisticsService {
    /**
     * Gets the user statistics for a given user.
     *
     * @param userId the ID of the user
     * @return the user statistics details
     */
    UserStatisticsDto getUserStatistics(UUID userId);
}
