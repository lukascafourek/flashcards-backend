package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.UserStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of {@link UserStatisticsService} for user statistics.
 */
@Service
@RequiredArgsConstructor
public class UserStatisticsServiceImpl implements UserStatisticsService {
    private final UserStatisticsRepository userStatisticsRepository;

    private final UserStatisticsMapper userStatisticsMapper;

    @Transactional
    @Override
    public BasicUserStatisticsDto getStatistics(UUID userId) {
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        return userStatisticsMapper.toDtoBasic(userStatistics);
    }

    @Transactional
    @Override
    public void incrementWantedStatistic(UUID userId, String statistic) {
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        userStatisticsMapper.incrementStatistic(userStatistics, statistic);
        userStatisticsRepository.save(userStatistics);
    }
}
