package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserStatisticsDto;

import java.util.UUID;

/**
 * Service for handling user statistics.
 */
public interface UserStatisticsService {
    void initializeUserStatistics(UUID userId);

    void updateUserStatistics(UUID userId, BasicUserStatisticsDto basicUserStatisticsDto);

    BasicUserStatisticsDto getUserStatistics(UUID userId);

    Integer getWantedUserStatistic(UUID userId, String wantedStatistic);
}
