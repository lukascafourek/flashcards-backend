package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Token;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.TokenRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Implementation of {@link TokenService}.
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public String generateToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (tokenRepository.existsById(user.getId())) {
            tokenRepository.deleteById(user.getId());
        }
        String rawToken = new Random().nextInt(10000000, 100000000) + "";
        Token token = new Token();
        token.setUser(user);
        token.setResetToken(passwordEncoder.encode(rawToken));
        token.setExpirationDate(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(token);
        return rawToken;
    }

    @Transactional
    @Override
    public void verifyToken(String email, String token) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Token tokenEntity = tokenRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
        if (!passwordEncoder.matches(token, tokenEntity.getResetToken())) {
            throw new IllegalArgumentException("Invalid token");
        }
        if (tokenEntity.getExpirationDate().isBefore(LocalDateTime.now())) {
            tokenRepository.deleteById(user.getId());
            throw new IllegalArgumentException("Token expired");
        }
        tokenRepository.deleteById(user.getId());
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void deleteExpiredTokens() {
        tokenRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }
}
