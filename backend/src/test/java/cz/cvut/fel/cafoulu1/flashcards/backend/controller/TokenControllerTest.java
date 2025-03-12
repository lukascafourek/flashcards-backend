package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.TokenServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private TokenServiceImpl tokenService;

    @InjectMocks
    private TokenController tokenController;

    public TokenControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendToken_shouldReturnOkWhenEmailExists() {
        Map<String, String> request = Map.of("email", "test@example.com");
        BasicUserDto user = new BasicUserDto(UUID.randomUUID(), "test@example.com", "testuser", AuthProvider.LOCAL);
        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(tokenService.sendToken(user.getId())).thenReturn("Token sent");

        ResponseEntity<?> response = tokenController.sendToken(request);

        assertEquals(ResponseEntity.ok("Token sent"), response);
    }

    @Test
    void sendToken_shouldReturnBadRequestWhenEmailDoesNotExist() {
        Map<String, String> request = Map.of("email", "nonexistent@example.com");
        when(userService.findByEmail("nonexistent@example.com")).thenThrow(new IllegalArgumentException("User not found"));

        ResponseEntity<?> response = tokenController.sendToken(request);

        assertEquals(ResponseEntity.badRequest().body("User not found"), response);
    }

    @Test
    void verifyToken_shouldReturnOkWhenTokenIsValid() {
        String email = "test@example.com";
        String token = "validToken";
        BasicUserDto user = new BasicUserDto(UUID.randomUUID(), email, "testuser", AuthProvider.LOCAL);
        when(userService.findByEmail(email)).thenReturn(user);
        doNothing().when(tokenService).verifyToken(user.getId(), token);

        ResponseEntity<?> response = tokenController.verifyToken(email, token);

        assertEquals(ResponseEntity.ok("Token verified"), response);
    }

    @Test
    void verifyToken_shouldReturnBadRequestWhenTokenIsInvalid() {
        String email = "test@example.com";
        String token = "invalidToken";
        BasicUserDto user = new BasicUserDto(UUID.randomUUID(), email, "testuser", AuthProvider.LOCAL);
        when(userService.findByEmail(email)).thenReturn(user);
        doThrow(new IllegalArgumentException("Invalid token")).when(tokenService).verifyToken(user.getId(), token);

        ResponseEntity<?> response = tokenController.verifyToken(email, token);

        assertEquals(ResponseEntity.badRequest().body("Invalid token"), response);
    }
}
