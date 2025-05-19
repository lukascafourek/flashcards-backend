package cz.cvut.fel.cafoulu1.flashcards.backend.service.emails;

/**
 * Service for sending a registration email to a new user.
 */
public interface EmailService {
    /**
     * Sends an email to the user with the given username (or token) and email address.
     *
     * @param param     given param (username for registration, token for password reset)
     * @param email     given email address
     */
    void sendEmail(String param, String email);
}
