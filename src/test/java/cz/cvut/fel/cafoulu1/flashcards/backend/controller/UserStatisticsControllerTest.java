package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserStatisticsServiceImpl;
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
import static org.mockito.Mockito.when;

public class UserStatisticsControllerTest {
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private UserStatisticsServiceImpl userStatisticsService;
    @InjectMocks
    private UserStatisticsController userStatisticsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserStatistics_returnsOkWithStatisticsWhenUserIsAuthenticated() {
        UUID userId = UUID.randomUUID();
        UserStatisticsDto statistics = new UserStatisticsDto();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(userStatisticsService.getUserStatistics(userId)).thenReturn(statistics);
        ResponseEntity<?> response = userStatisticsController.getUserStatistics(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(statistics, response.getBody());
    }

    @Test
    void getUserStatistics_returnsBadRequestWhenExceptionIsThrown() {
        UUID userId = UUID.randomUUID();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(userStatisticsService.getUserStatistics(userId)).thenThrow(new RuntimeException("Testing error"));
        ResponseEntity<?> response = userStatisticsController.getUserStatistics(authentication);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
    }
}
