package cz.cvut.fel.cafoulu1.flashcards.backend.service;

/**
 * Service for handling password reset tokens and sending emails with them.
 */
public interface TokenService {
    String generateToken(String email);

    void verifyToken(String email, String token);
}
