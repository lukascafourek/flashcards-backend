package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.UserStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This is a service for handling user statistics requests. It is used for initializing, updating and getting user statistics.
 */
@Service
@RequiredArgsConstructor
public class UserStatisticsServiceImpl implements UserStatisticsService {
    private final UserStatisticsRepository userStatisticsRepository;

    private final UserStatisticsMapper userStatisticsMapper;

    @Override
    public void initializeUserStatistics(UUID userId) {
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setUserId(userId);
        userStatisticsRepository.save(userStatistics);
    }

    @Override
    public void updateUserStatistics(UUID userId, BasicUserStatisticsDto basicUserStatisticsDto) {
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        UserStatistics updatedUserStatistics = userStatisticsMapper.partialUpdateBasic(basicUserStatisticsDto, userStatistics);
        userStatisticsRepository.save(updatedUserStatistics);
    }

    @Override
    public BasicUserStatisticsDto getUserStatistics(UUID userId) {
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        return userStatisticsMapper.toDtoBasic(userStatistics);
    }

    @Override
    public Integer getWantedUserStatistic(UUID userId, String wantedStatistic) {
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        return userStatisticsMapper.getWantedUserStatistic(userStatistics, wantedStatistic);
    }
}
