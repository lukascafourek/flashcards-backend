package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;

import java.util.UUID;

/**
 * Service for handling password reset tokens and sending emails with them.
 */
public interface TokenService {
    String generateToken(User user);

    String sendToken(UUID userId);

    void verifyToken(UUID userId, String token);
}
