package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.TokenServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for handling token requests and verification of tokens.
 */
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {
    private final TokenServiceImpl tokenService;

    private final UserServiceImpl userService;

    @PostMapping("/request-reset")
    public ResponseEntity<?> sendToken(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            BasicUserDto user = userService.findByEmail(email);
            String message = tokenService.sendToken(user.getId());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestParam String email, @RequestParam String token) {
        try {
            BasicUserDto user = userService.findByEmail(email);
            tokenService.verifyToken(user.getId(), token);
            return ResponseEntity.ok("Token verified");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
