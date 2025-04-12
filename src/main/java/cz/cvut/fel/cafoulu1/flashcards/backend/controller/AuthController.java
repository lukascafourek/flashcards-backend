package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.LoginRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.UpdateUserRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.security.jwtconfig.JwtUtils;
import cz.cvut.fel.cafoulu1.flashcards.backend.security.response.CookieSetup;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.RegistrationEmailImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * This is a controller for handling authentication requests.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userService;

    private final RegistrationEmailImpl registrationEmail;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerUser(registerRequest);
            String email = registerRequest.getEmail();
            String username = registerRequest.getUsername();
            registrationEmail.sendEmail(username, email);
            return ResponseEntity.ok("User registered successfully.");
        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            CookieSetup.setCookies(response, authentication, jwtUtils);
            return ResponseEntity.ok("User logged in successfully.");
        } catch (Exception e) {
            logger.error("Error during user login: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(userService.findById(userDetails.getId()));
        } catch (Exception e) {
            logger.error("Error during fetching current user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.findAll());
        } catch (Exception e) {
            logger.error("Error during fetching all users: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/update-user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userService.updateUser(userDetails.getUsername(), updateUserRequest);
            return ResponseEntity.ok("User updated successfully.");
        } catch (Exception e) {
            logger.error("Error during updating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/update-user/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUserByAdmin(@PathVariable("email") String email, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            userService.updateUser(email, updateUserRequest);
            return ResponseEntity.ok("User updated successfully.");
        } catch (Exception e) {
            logger.error("Error during updating user by admin: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteAccount(Authentication authentication, HttpServletResponse response) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userService.deleteUser(userDetails.getId());
            CookieSetup.unsetCookies(response);
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (Exception e) {
            logger.error("Error during deleting account: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-account/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAccountByAdmin(@PathVariable("userId") UUID userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (Exception e) {
            logger.error("Error during deleting account by admin: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        try {
            CookieSetup.unsetCookies(response);
            return ResponseEntity.ok("User logged out successfully.");
        } catch (Exception e) {
            logger.error("Error during user logout: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam @Size(max = 255) String email, @RequestParam @Size(max = 255) String password) {
        try {
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.setPassword(password);
            userService.updateUser(email, updateUserRequest);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            logger.error("Error during password reset: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
