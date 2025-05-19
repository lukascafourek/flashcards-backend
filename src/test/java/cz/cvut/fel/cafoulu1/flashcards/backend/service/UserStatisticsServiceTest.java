package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.UserStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
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

class UserStatisticsServiceTest {
    @Mock
    private UserStatisticsRepository userStatisticsRepository;
    @Mock
    private UserStatisticsMapper userStatisticsMapper;
    @InjectMocks
    private UserStatisticsServiceImpl userStatisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserStatistics_returnsMappedDtoWhenUserStatisticsExist() {
        UUID userId = UUID.randomUUID();
        UserStatistics userStatistics = new UserStatistics();
        UserStatisticsDto expectedDto = new UserStatisticsDto();
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.of(userStatistics));
        when(userStatisticsMapper.toDto(userStatistics)).thenReturn(expectedDto);
        UserStatisticsDto result = userStatisticsService.getUserStatistics(userId);
        assertEquals(expectedDto, result);
        verify(userStatisticsRepository).findById(userId);
        verify(userStatisticsMapper).toDto(userStatistics);
    }

    @Test
    void getUserStatistics_throwsExceptionWhenUserStatisticsNotFound() {
        UUID userId = UUID.randomUUID();
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userStatisticsService.getUserStatistics(userId));
        assertEquals("User statistics not found", exception.getMessage());
        verify(userStatisticsRepository).findById(userId);
        verifyNoInteractions(userStatisticsMapper);
    }
}
