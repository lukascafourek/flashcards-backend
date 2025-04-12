package cz.cvut.fel.cafoulu1.flashcards.backend.exception;

/**
 * Exception thrown when two values are not the same.
 */
public class ValueNotSameException extends RuntimeException {
    public ValueNotSameException(String message) {
        super(message);
    }
}
