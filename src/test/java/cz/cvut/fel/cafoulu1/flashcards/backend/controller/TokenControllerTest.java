package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.service.TokenServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.ResetPasswordEmailImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenControllerTest {
    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private ResetPasswordEmailImpl resetPasswordEmail;
    @InjectMocks
    private TokenController tokenController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendToken_returnsOkWhenTokenIsSentSuccessfully() {
        Map<String, String> request = Map.of("email", "test@example.com");
        String token = "generatedToken";
        when(tokenService.generateToken("test@example.com")).thenReturn(token);
        doNothing().when(resetPasswordEmail).sendEmail(token, "test@example.com");
        ResponseEntity<?> response = tokenController.sendToken(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tokenService).generateToken("test@example.com");
        verify(resetPasswordEmail).sendEmail(token, "test@example.com");
    }

    @Test
    void sendToken_returnsBadRequestWhenEmailIsInvalid() {
        Map<String, String> request = Map.of("email", "");
        ResponseEntity<?> response = tokenController.sendToken(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email is required (max 255 characters)", response.getBody());
        verifyNoInteractions(tokenService, resetPasswordEmail);
    }

    @Test
    void sendToken_returnsBadRequestWhenExceptionIsThrown() {
        Map<String, String> request = Map.of("email", "test@example.com");
        when(tokenService.generateToken("test@example.com")).thenThrow(new RuntimeException("Testing error"));
        ResponseEntity<?> response = tokenController.sendToken(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(tokenService).generateToken("test@example.com");
        verifyNoInteractions(resetPasswordEmail);
    }

    @Test
    void verifyToken_returnsOkWhenTokenIsValid() {
        String email = "test@example.com";
        String token = "validToken";
        doNothing().when(tokenService).verifyToken(email, token);
        ResponseEntity<?> response = tokenController.verifyToken(email, token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tokenService).verifyToken(email, token);
    }

    @Test
    void verifyToken_returnsBadRequestWhenTokenIsInvalid() {
        String email = "test@example.com";
        String token = "invalidToken";
        doThrow(new RuntimeException("Testing error")).when(tokenService).verifyToken(email, token);
        ResponseEntity<?> response = tokenController.verifyToken(email, token);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(tokenService).verifyToken(email, token);
    }
}
