package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.service.SetStatisticsServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SetStatisticsControllerTest {
    @Mock
    private SetStatisticsServiceImpl setStatisticsService;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;
    @InjectMocks
    private SetStatisticsController setStatisticsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void incrementSetStatistics_returnsOkWhenStatisticsAreIncrementedSuccessfully() {
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String firstStat = "views";
        String secondStat = "likes";

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doNothing().when(setStatisticsService).incrementWantedStatistics(setId, userId, firstStat, secondStat);

        ResponseEntity<?> response = setStatisticsController.incrementSetStatistics(setId, firstStat, secondStat, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Wanted set statistic incremented", response.getBody());
        verify(setStatisticsService).incrementWantedStatistics(setId, userId, firstStat, secondStat);
    }

    @Test
    void incrementSetStatistics_returnsBadRequestWhenExceptionIsThrown() {
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String firstStat = "views";

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doThrow(new RuntimeException("Testing error")).when(setStatisticsService).incrementWantedStatistics(setId, userId, firstStat, null);

        ResponseEntity<?> response = setStatisticsController.incrementSetStatistics(setId, firstStat, null, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(setStatisticsService).incrementWantedStatistics(setId, userId, firstStat, null);
    }
}
