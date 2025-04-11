package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserStatisticsServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * This is a controller for handling user statistics requests.
 * It is used for getting user statistics.
 */
@RestController
@RequestMapping("/user-statistics")
@RequiredArgsConstructor
public class UserStatisticsController {
    private final UserStatisticsServiceImpl userStatisticsService;

    private static final Logger logger = LogManager.getLogger(UserStatisticsController.class);

    @GetMapping("/values")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserStatistics(Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(userStatisticsService.getUserStatistics(userDetails.getId()));
        } catch (Exception e) {
            logger.error("Error while getting user statistics: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
