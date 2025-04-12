package cz.cvut.fel.cafoulu1.flashcards.backend.exception;

/**
 * Exception thrown when an entity already exists in the database.
 */
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
