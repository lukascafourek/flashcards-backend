package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.UserStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of {@link GenericStatisticsService} for user statistics.
 */
@Service
@RequiredArgsConstructor
public class UserStatisticsServiceImpl implements GenericStatisticsService<BasicUserStatisticsDto> {
    private final UserStatisticsRepository userStatisticsRepository;

    private final UserStatisticsMapper userStatisticsMapper;

    @Override
    public BasicUserStatisticsDto getStatistics(UUID userId) {
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        return userStatisticsMapper.toDtoBasic(userStatistics);
    }
}
