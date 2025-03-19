package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import java.util.UUID;

/**
 * Generic service for handling statistics requests. It is used for getting statistics.
 * @param <T> type of statistics dto
 */
public interface GenericStatisticsService <T> {
    T getStatistics(UUID id);
}
