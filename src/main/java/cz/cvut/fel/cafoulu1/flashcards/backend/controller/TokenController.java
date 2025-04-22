package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.service.TokenServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.ResetPasswordEmailImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(TokenController.class);

    @PostMapping("/request-reset")
    @Operation(summary = "Request a password reset token")
    public ResponseEntity<?> sendToken(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.isEmpty() || email.length() > 255) {
                throw new IllegalArgumentException("Email is required (max 255 characters)");
            }
            String token = tokenService.generateToken(email);
            resetPasswordEmail.sendEmail(token, email);
            return ResponseEntity.ok("Token sent successfully");
        } catch (Exception e) {
            logger.error("Error sending token: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    @Operation(summary = "Verify a password reset token")
    public ResponseEntity<?> verifyToken(@RequestParam @Size(max = 255) String email, @RequestParam @Size(max = 255) String token) {
        try {
            tokenService.verifyToken(email, token);
            return ResponseEntity.ok("Token verified");
        } catch (Exception e) {
            logger.error("Error verifying token: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
