package cz.cvut.fel.cafoulu1.flashcards.backend.service.emails;

/**
 * Service for sending a registration email to a new user.
 */
public interface EmailService {
    /**
     * Sends an email to the user with the given username (or token) and email address.
     *
     * @param username  given username (in ResetPasswordEmailImpl it is token)
     * @param email     given email address
     */
    void sendEmail(String username, String email);
}
