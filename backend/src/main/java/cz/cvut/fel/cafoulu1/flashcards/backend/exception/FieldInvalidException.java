package cz.cvut.fel.cafoulu1.flashcards.backend.exception;

/**
 * Exception thrown when a field is invalid.
 */
public class FieldInvalidException extends RuntimeException {
    public FieldInvalidException(String message) {
        super(message);
    }
}
