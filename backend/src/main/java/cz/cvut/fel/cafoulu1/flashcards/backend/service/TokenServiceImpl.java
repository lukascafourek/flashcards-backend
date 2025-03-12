package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Token;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.TokenRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.ResetPasswordEmailImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * Service for handling password reset tokens and sending emails with them.
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final ResetPasswordEmailImpl resetPasswordEmail;

    @Override
    public String generateToken(User user) {
        String rawToken = new Random().nextInt(10000000, 100000000) + "";
        Token token = new Token();
        token.setUserId(user.getId());
        token.setUser(user);
        token.setResetToken(passwordEncoder.encode(rawToken));
        token.setExpirationDate(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(token);
        return rawToken;
    }

    @Override
    public String sendToken(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (tokenRepository.existsById(user.getId())) {
            tokenRepository.deleteById(userId);
        }
        String token = generateToken(user);
        return resetPasswordEmail.sendEmail(token, user);
    }

    @Override
    public void verifyToken(UUID userId, String token) {
        Token tokenEntity = tokenRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
        if (!passwordEncoder.matches(token, tokenEntity.getResetToken())) {
            throw new IllegalArgumentException("Invalid token");
        }
        if (tokenEntity.getExpirationDate().isBefore(LocalDateTime.now())) {
            tokenRepository.deleteById(userId);
            throw new IllegalArgumentException("Token expired");
        }
        tokenRepository.deleteById(userId);
    }

    @Scheduled(fixedRate = 900000)
    public void deleteExpiredTokens() {
        tokenRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }
}
