package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.SetStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.UserStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.SetStatisticsRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserStatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SetStatisticsServiceTest {
    @Mock
    private SetStatisticsRepository setStatisticsRepository;
    @Mock
    private UserStatisticsRepository userStatisticsRepository;
    @Mock
    private SetStatisticsMapper setStatisticsMapper;
    @Mock
    private UserStatisticsMapper userStatisticsMapper;
    @InjectMocks
    private SetStatisticsServiceImpl setStatisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void incrementWantedStatistics_incrementsBothStatisticsWhenSecondStatIsProvided() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String firstStat = "stat1";
        String secondStat = "stat2";
        SetStatistics setStatistics = new SetStatistics();
        UserStatistics userStatistics = new UserStatistics();

        when(setStatisticsRepository.findByCardSetIdAndUserId(cardSetId, userId)).thenReturn(Optional.of(setStatistics));
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.of(userStatistics));

        setStatisticsService.incrementWantedStatistics(cardSetId, userId, firstStat, secondStat);

        verify(setStatisticsMapper).incrementStatistic(setStatistics, firstStat);
        verify(userStatisticsMapper).incrementStatistic(userStatistics, firstStat);
        verify(setStatisticsMapper).incrementStatistic(setStatistics, secondStat);
        verify(userStatisticsMapper).incrementStatistic(userStatistics, secondStat);
        verify(setStatisticsRepository).save(setStatistics);
        verify(userStatisticsRepository).save(userStatistics);
    }

    @Test
    void incrementWantedStatistics_incrementsOnlyFirstStatisticWhenSecondStatIsNull() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String firstStat = "stat1";
        String secondStat = null;
        SetStatistics setStatistics = new SetStatistics();
        UserStatistics userStatistics = new UserStatistics();

        when(setStatisticsRepository.findByCardSetIdAndUserId(cardSetId, userId)).thenReturn(Optional.of(setStatistics));
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.of(userStatistics));

        setStatisticsService.incrementWantedStatistics(cardSetId, userId, firstStat, secondStat);

        verify(setStatisticsMapper).incrementStatistic(setStatistics, firstStat);
        verify(userStatisticsMapper).incrementStatistic(userStatistics, firstStat);
        verify(setStatisticsRepository).save(setStatistics);
        verify(userStatisticsRepository).save(userStatistics);
    }

    @Test
    void incrementWantedStatistics_throwsExceptionWhenSetStatisticsNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String firstStat = "stat1";
        String secondStat = "stat2";

        when(setStatisticsRepository.findByCardSetIdAndUserId(cardSetId, userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> setStatisticsService.incrementWantedStatistics(cardSetId, userId, firstStat, secondStat));
        assertEquals("Set statistics not found", exception.getMessage());
        verifyNoInteractions(userStatisticsRepository, setStatisticsMapper, userStatisticsMapper);
    }

    @Test
    void incrementWantedStatistics_throwsExceptionWhenUserStatisticsNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String firstStat = "stat1";
        String secondStat = "stat2";
        SetStatistics setStatistics = new SetStatistics();

        when(setStatisticsRepository.findByCardSetIdAndUserId(cardSetId, userId)).thenReturn(Optional.of(setStatistics));
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> setStatisticsService.incrementWantedStatistics(cardSetId, userId, firstStat, secondStat));
        assertEquals("User statistics not found", exception.getMessage());
        verifyNoInteractions(setStatisticsMapper, userStatisticsMapper);
    }
}
