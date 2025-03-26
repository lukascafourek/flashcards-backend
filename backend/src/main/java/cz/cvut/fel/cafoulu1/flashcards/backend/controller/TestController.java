package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// test for a connection with frontend
@RestController("/test")
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        try {
            return ResponseEntity.ok("Test successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
