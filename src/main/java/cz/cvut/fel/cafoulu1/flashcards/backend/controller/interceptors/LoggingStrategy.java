package cz.cvut.fel.cafoulu1.flashcards.backend.controller.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Interface for logging strategies.
 */
public interface LoggingStrategy {
    /**
     * Logger instance for logging.
     */
    Logger log = LogManager.getLogger(LoggingStrategy.class);

    /**
     * Logs the request before it is handled.
     *
     * @param request the HTTP request
     */
    void logPreHandle(HttpServletRequest request);

    /**
     * Logs the request after it has been handled.
     *
     * @param request the HTTP request
     */
    void logPostHandle(HttpServletRequest request);
}
