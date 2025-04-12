package cz.cvut.fel.cafoulu1.flashcards.backend.exception;

/**
 * Exception thrown when a field is missing.
 */
public class FieldMissingException extends RuntimeException {
    public FieldMissingException(String message) {
        super(message);
    }
}
