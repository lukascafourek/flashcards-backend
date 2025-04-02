package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.service.TokenServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.ResetPasswordEmailImpl;
import jakarta.validation.constraints.Size;
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

    private final ResetPasswordEmailImpl resetPasswordEmail;

    @PostMapping("/request-reset")
    public ResponseEntity<?> sendToken(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.isEmpty() || email.length() > 255) {
                return ResponseEntity.badRequest().body("Email is required (max 255 characters)");
            }
            String token = tokenService.generateToken(email);
            resetPasswordEmail.sendEmail(token, email);
            return ResponseEntity.ok("Token sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestParam @Size(max = 255) String email, @RequestParam @Size(max = 255) String token) {
        try {
            tokenService.verifyToken(email, token);
            return ResponseEntity.ok("Token verified");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
