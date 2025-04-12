package cz.cvut.fel.cafoulu1.flashcards.backend.exception;

/**
 * Exception thrown when an entity is not found.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
