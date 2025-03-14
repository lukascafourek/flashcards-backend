package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.service.TokenServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.ResetPasswordEmailImpl;
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
            String token = tokenService.generateToken(email);
            resetPasswordEmail.sendEmail(token, email);
            return ResponseEntity.ok("Token sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestParam String email, @RequestParam String token) {
        try {
            tokenService.verifyToken(email, token);
            return ResponseEntity.ok("Token verified");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
