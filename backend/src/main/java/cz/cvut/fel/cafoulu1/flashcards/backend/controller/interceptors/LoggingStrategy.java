package cz.cvut.fel.cafoulu1.flashcards.backend.controller.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface for logging strategies.
 */
public interface LoggingStrategy {
    Logger log = LoggerFactory.getLogger(LoggingStrategy.class);

    void logPreHandle(HttpServletRequest request);

    void logPostHandle(HttpServletRequest request);
}
