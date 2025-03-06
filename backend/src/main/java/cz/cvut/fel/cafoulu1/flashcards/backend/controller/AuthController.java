package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.LoginRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.security.jwtconfig.JwtUtils;
//import cz.cvut.fel.cafoulu1.flashcards.backend.security.response.JwtResponse;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserService;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
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

import java.io.UnsupportedEncodingException;
import java.time.Duration;

/**
 * This is a controller for handling authentication requests.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) throws UnsupportedEncodingException {
        BasicUserDto user = userService.registerUser(registerRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws UnsupportedEncodingException {
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
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicUserDto> getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(userService.findById(userDetails.getId()));
    }

    @PutMapping("/update-email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateEmail(@RequestParam String email, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userService.updateEmail(userDetails.getId(), email);
        return ResponseEntity.ok("Email updated successfully.");
    }

    @PutMapping("/update-username")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUsername(@RequestParam String username, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userService.updateUsername(userDetails.getId(), username);
        return ResponseEntity.ok("Username updated successfully.");
    }

    @PutMapping("/update-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updatePassword(@RequestParam String password, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userService.updatePassword(userDetails.getId(), password);
        return ResponseEntity.ok("Password updated successfully.");
    }

    @DeleteMapping("/delete-account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteAccount(Authentication authentication, HttpServletResponse response) {
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser(HttpServletResponse response, Authentication authentication) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)  // Nastav na true, pokud používáš HTTPS
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return ResponseEntity.ok("User logged out successfully.");
    }
}
