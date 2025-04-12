package cz.cvut.fel.cafoulu1.flashcards.backend.controller.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Short logging strategy for logging only basic information about request.
 */
@Component
@Scope("singleton")
public class ShortLoggingStrategy implements LoggingStrategy {
    @Override
    public void logPreHandle(HttpServletRequest request) {
        log.info("{}, sessionID:{}", request.getRequestURI(), request.getSession().getId());
    }

    @Override
    public void logPostHandle(HttpServletRequest request) {
    }
}
