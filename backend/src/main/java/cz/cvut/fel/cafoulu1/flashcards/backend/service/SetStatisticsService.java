package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicSetStatisticsDto;

import java.util.UUID;

/**
 * Set statistics service for handling set statistics requests. It is used for incrementing wanted set statistics.
 * @see GenericStatisticsService
 */
public interface SetStatisticsService extends GenericStatisticsService<BasicSetStatisticsDto> {
    void incrementWantedStatistic(UUID cardSetId, UUID userId, String statistic);
}
