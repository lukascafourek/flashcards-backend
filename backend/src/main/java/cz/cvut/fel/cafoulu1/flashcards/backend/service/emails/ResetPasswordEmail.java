package cz.cvut.fel.cafoulu1.flashcards.backend.service.emails;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;

/**
 * Service for sending reset password email to user with token
 */
public interface ResetPasswordEmail {
    String sendEmail(String token, User user);
}
