package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.service.SetStatisticsServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * This is a controller for handling set statistics requests. It is used for incrementing wanted set statistics.
 */
@RestController
@RequestMapping("/card-sets/{set_id}/set-statistics")
@RequiredArgsConstructor
public class SetStatisticsController {
    private final SetStatisticsServiceImpl setStatisticsService;

    @PatchMapping("/increment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> incrementSetStatistics(@PathVariable("set_id") UUID setId, @RequestParam String statistic, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            setStatisticsService.incrementWantedStatistic(setId, userDetails.getId(), statistic);
            return ResponseEntity.ok("Wanted set statistic incremented");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
