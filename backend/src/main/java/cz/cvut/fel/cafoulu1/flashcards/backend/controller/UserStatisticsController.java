package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserStatisticsServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * This is a controller for handling user statistics requests.
 * It is used for getting user statistics and incrementing wanted user statistics.
 */
@RestController
@RequestMapping("/user-statistics")
@RequiredArgsConstructor
public class UserStatisticsController {
    private final UserStatisticsServiceImpl userStatisticsService;

    @GetMapping("/values")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserStatistics(Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(userStatisticsService.getStatistics(userDetails.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/increment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> incrementUserStatistics(@RequestParam String statistic, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userStatisticsService.incrementWantedStatistic(userDetails.getId(), statistic);
            return ResponseEntity.ok("Wanted user statistic incremented");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
