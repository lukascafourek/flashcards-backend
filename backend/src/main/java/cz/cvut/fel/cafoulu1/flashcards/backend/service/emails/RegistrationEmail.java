package cz.cvut.fel.cafoulu1.flashcards.backend.service.emails;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;

/**
 * Service for sending a registration email to a new user.
 */
public interface RegistrationEmail {
    void sendEmail(User user);
}
