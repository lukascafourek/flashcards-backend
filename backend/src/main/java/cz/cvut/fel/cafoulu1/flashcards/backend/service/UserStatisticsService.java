package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserStatisticsDto;

import java.util.UUID;

/**
 * User statistics service for handling user statistics requests. It is used for incrementing wanted user statistics.
 * @see GenericStatisticsService
 */
public interface UserStatisticsService extends GenericStatisticsService<BasicUserStatisticsDto> {
    void incrementWantedStatistic(UUID userId, String statistic);
}
