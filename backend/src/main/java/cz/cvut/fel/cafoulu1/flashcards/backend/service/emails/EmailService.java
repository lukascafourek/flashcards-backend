package cz.cvut.fel.cafoulu1.flashcards.backend.service.emails;

/**
 * Service for sending a registration email to a new user.
 */
public interface EmailService {
    void sendEmail(String email, String username);
}
