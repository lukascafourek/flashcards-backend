package cz.cvut.fel.cafoulu1.flashcards.backend.service;

/**
 * Service for handling password reset tokens and sending emails with them.
 */
public interface TokenService {
    /**
     * Generates a token for the given email address.
     *
     * @param email the email address to generate a token for
     * @return the generated token
     */
    String generateToken(String email);

    /**
     * Verifies the token for the given email address.
     *
     * @param email the email address to verify the token for
     * @param token the token to verify
     */
    void verifyToken(String email, String token);
}
