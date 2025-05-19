package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.SetStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.UserStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.SetStatisticsRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


/**
 * Implementation of {@link StatisticsService} for set statistics.
 */
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final SetStatisticsRepository setStatisticsRepository;

    private final UserStatisticsRepository userStatisticsRepository;

    private final SetStatisticsMapper setStatisticsMapper;

    private final UserStatisticsMapper userStatisticsMapper;

    @Transactional
    @Override
    public void incrementWantedStatistics(UUID cardSetId, UUID userId, String firstStat, String secondStat) {
        SetStatistics setStatistics = setStatisticsRepository.findByCardSetIdAndUserId(cardSetId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Set statistics not found"));
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        setStatisticsMapper.incrementStatistic(setStatistics, firstStat);
        userStatisticsMapper.incrementStatistic(userStatistics, firstStat);
        if (secondStat != null && !secondStat.isEmpty()) {
            setStatisticsMapper.incrementStatistic(setStatistics, secondStat);
            userStatisticsMapper.incrementStatistic(userStatistics, secondStat);
        }
        setStatisticsRepository.save(setStatistics);
        userStatisticsRepository.save(userStatistics);
    }
}
