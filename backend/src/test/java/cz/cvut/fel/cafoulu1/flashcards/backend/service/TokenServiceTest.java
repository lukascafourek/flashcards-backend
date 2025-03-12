package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Token;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.TokenRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.ResetPasswordEmailImpl;
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

    @Mock
    private ResetPasswordEmailImpl resetPasswordEmail;

    @InjectMocks
    private TokenServiceImpl tokenService;

    public TokenServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateToken_shouldReturnTokenWhenUserIsValid() {
        User user = new User();
        user.setId(UUID.randomUUID());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedToken");
        when(tokenRepository.save(any(Token.class))).thenReturn(new Token());

        String token = tokenService.generateToken(user);

        assertNotNull(token);
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void sendToken_shouldReturnEmailResponseWhenUserExists() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tokenRepository.existsById(userId)).thenReturn(false);
        when(resetPasswordEmail.sendEmail(anyString(), eq(user))).thenReturn("Reset password email sent successfully");

        String response = tokenService.sendToken(userId);

        assertEquals("Reset password email sent successfully", response);
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void sendToken_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> tokenService.sendToken(userId));
    }

    @Test
    void verifyToken_shouldThrowExceptionWhenTokenNotFound() {
        UUID userId = UUID.randomUUID();
        when(tokenRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> tokenService.verifyToken(userId, "token"));
    }

    @Test
    void verifyToken_shouldThrowExceptionWhenTokenIsInvalid() {
        UUID userId = UUID.randomUUID();
        Token tokenEntity = new Token();
        tokenEntity.setResetToken("encodedToken");
        when(tokenRepository.findById(userId)).thenReturn(Optional.of(tokenEntity));
        when(passwordEncoder.matches(anyString(), eq("encodedToken"))).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> tokenService.verifyToken(userId, "invalidToken"));
    }

//    @Test
//    void verifyToken_shouldThrowExceptionWhenTokenIsExpired() {
//        UUID userId = UUID.randomUUID();
//        Token tokenEntity = new Token();
//        tokenEntity.setExpirationDate(LocalDateTime.now().minusMinutes(1));
//        when(tokenRepository.findById(userId)).thenReturn(Optional.of(tokenEntity));
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
//
//        assertThrows(IllegalArgumentException.class, () -> tokenService.verifyToken(userId, "token"));
//    }

    @Test
    void verifyToken_shouldThrowExceptionWhenTokenIsExpired() {
        UUID userId = UUID.randomUUID();
        Token tokenEntity = new Token();
        tokenEntity.setResetToken("encodedToken");
        tokenEntity.setExpirationDate(LocalDateTime.now().minusMinutes(1));
        when(tokenRepository.findById(userId)).thenReturn(Optional.of(tokenEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> tokenService.verifyToken(userId, "token"));
        verify(tokenRepository).deleteById(userId);
    }

    @Test
    void verifyToken_shouldDeleteTokenWhenValid() {
        UUID userId = UUID.randomUUID();
        Token tokenEntity = new Token();
        tokenEntity.setResetToken("encodedToken");
        tokenEntity.setExpirationDate(LocalDateTime.now().plusMinutes(1));
        when(tokenRepository.findById(userId)).thenReturn(Optional.of(tokenEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        tokenService.verifyToken(userId, "token");

        verify(tokenRepository).deleteById(userId);
    }
}
