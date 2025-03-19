package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicSetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.SetStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.SetStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of {@link GenericStatisticsService} for set statistics.
 */
@Service
@RequiredArgsConstructor
public class SetStatisticsServiceImpl implements GenericStatisticsService<BasicSetStatisticsDto> {
    private final SetStatisticsRepository setStatisticsRepository;

    private final SetStatisticsMapper setStatisticsMapper;

    @Override
    public BasicSetStatisticsDto getStatistics(UUID id) {
        SetStatistics setStatistics = setStatisticsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Set statistics not found"));
        return setStatisticsMapper.toDtoBasic(setStatistics);
    }
}
