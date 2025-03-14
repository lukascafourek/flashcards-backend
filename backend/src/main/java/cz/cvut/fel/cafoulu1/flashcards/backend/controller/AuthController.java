package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.LoginRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.UpdateUserRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.security.jwtconfig.JwtUtils;
//import cz.cvut.fel.cafoulu1.flashcards.backend.security.response.JwtResponse;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.EmailServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

/**
 * This is a controller for handling authentication requests.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userService;

    private final EmailServiceImpl registrationEmail;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerUser(registerRequest);
            String email = registerRequest.getEmail();
            String username = registerRequest.getUsername();
            registrationEmail.sendEmail(email, username);
            return ResponseEntity.ok("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(authentication);
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)
                    .secure(false) // Nastav na `true`, pokud jedeš přes HTTPS!
                    .path("/")
                    .maxAge(Duration.ofDays(1))
//                .domain("localhost")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            return ResponseEntity.ok("User logged in successfully.");
//        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicUserDto> getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(userService.findById(userDetails.getId()));
    }

    @PatchMapping("/update-user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest updateUserRequest, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userService.updateUser(userDetails.getUsername(), updateUserRequest);
            return ResponseEntity.ok("User updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PutMapping("/update-email")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<String> updateEmail(@RequestParam String email, Authentication authentication) {
//        try {
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//            userService.updateEmail(userDetails.getId(), email);
//            return ResponseEntity.ok("Email updated successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @PutMapping("/update-username")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<String> updateUsername(@RequestParam String username, Authentication authentication) {
//        try {
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//            userService.updateUsername(userDetails.getId(), username);
//            return ResponseEntity.ok("Username updated successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @PutMapping("/update-password")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<String> updatePassword(@RequestParam String password, Authentication authentication) {
//        try {
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//            userService.updatePassword(userDetails.getId(), password);
//            return ResponseEntity.ok("Password updated successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @DeleteMapping("/delete-account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteAccount(Authentication authentication, HttpServletResponse response) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userService.deleteUser(userDetails.getId());
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                    .httpOnly(true)
                    .secure(false)  // Nastav na true, pokud používáš HTTPS
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/email-exists")
    public ResponseEntity<Boolean> emailExists(@RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    @GetMapping("/check-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> checkPassword(@RequestParam String password, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(userService.checkPassword(userDetails.getId(), password));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        try {
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                    .httpOnly(true)
                    .secure(false)  // Nastav na true, pokud používáš HTTPS
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            return ResponseEntity.ok("User logged out successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String password) {
        try {
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.setPassword(password);
            userService.updateUser(email, updateUserRequest);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
