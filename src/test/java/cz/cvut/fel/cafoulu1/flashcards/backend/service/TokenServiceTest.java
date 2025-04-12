package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Token;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.TokenRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateToken_createsNewTokenForUser() {
        String email = "user@example.com";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenRepository.existsById(user.getId())).thenReturn(false);

        String token = tokenService.generateToken(email);

        assertNotNull(token);
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void generateToken_replacesExistingTokenForUser() {
        String email = "user@example.com";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenRepository.existsById(user.getId())).thenReturn(true);

        String token = tokenService.generateToken(email);

        assertNotNull(token);
        verify(tokenRepository).deleteById(user.getId());
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void verifyToken_validTokenDoesNotThrowException() {
        String email = "user@example.com";
        String rawToken = "12345678";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        Token token = new Token();
        token.setUser(user);
        token.setResetToken(passwordEncoder.encode(rawToken));
        token.setExpirationDate(LocalDateTime.now().plusMinutes(15));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenRepository.findById(user.getId())).thenReturn(Optional.of(token));
        when(passwordEncoder.matches(rawToken, token.getResetToken())).thenReturn(true);

        assertDoesNotThrow(() -> tokenService.verifyToken(email, rawToken));
        verify(tokenRepository).deleteById(user.getId());
    }

    @Test
    void verifyToken_invalidTokenThrowsException() {
        String email = "user@example.com";
        String rawToken = "12345678";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        Token token = new Token();
        token.setUser(user);
        token.setResetToken(passwordEncoder.encode("87654321"));
        token.setExpirationDate(LocalDateTime.now().plusMinutes(15));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenRepository.findById(user.getId())).thenReturn(Optional.of(token));
        when(passwordEncoder.matches(rawToken, token.getResetToken())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> tokenService.verifyToken(email, rawToken));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void verifyToken_expiredTokenThrowsException() {
        String email = "user@example.com";
        String rawToken = "12345678";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        Token token = new Token();
        token.setUser(user);
        token.setResetToken(passwordEncoder.encode(rawToken));
        token.setExpirationDate(LocalDateTime.now().minusMinutes(1));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenRepository.findById(user.getId())).thenReturn(Optional.of(token));
        when(passwordEncoder.matches(rawToken, token.getResetToken())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> tokenService.verifyToken(email, rawToken));
        assertEquals("Token expired", exception.getMessage());
        verify(tokenRepository).deleteById(user.getId());
    }
}
