package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicSetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.SetStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.SetStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of {@link SetStatisticsService} for set statistics.
 */
@Service
@RequiredArgsConstructor
public class SetStatisticsServiceImpl implements SetStatisticsService {
    private final SetStatisticsRepository setStatisticsRepository;

    private final SetStatisticsMapper setStatisticsMapper;

    @Transactional
    @Override
    public BasicSetStatisticsDto getStatistics(UUID id) {
        SetStatistics setStatistics = setStatisticsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Set statistics not found"));
        return setStatisticsMapper.toDtoBasic(setStatistics);
    }

    @Transactional
    @Override
    public void incrementWantedStatistic(UUID cardSetId, UUID userId, String statistic) {
        SetStatistics setStatistics = setStatisticsRepository.findByCardSetIdAndUserId(cardSetId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Set statistics not found"));
        setStatisticsMapper.incrementStatistic(setStatistics, statistic);
        setStatisticsRepository.save(setStatistics);
    }
}
