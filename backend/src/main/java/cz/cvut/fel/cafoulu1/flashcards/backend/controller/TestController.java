package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller for testing if backend is running.
 */
@RestController("/test")
public class TestController {
    private static final Logger logger = LogManager.getLogger(TestController.class);

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        try {
            return ResponseEntity.ok("Test successful.");
        } catch (Exception e) {
            logger.error("Error during connection test: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
