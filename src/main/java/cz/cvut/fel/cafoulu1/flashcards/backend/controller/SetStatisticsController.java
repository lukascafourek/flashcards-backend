package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.service.SetStatisticsServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * This is a controller for handling set statistics requests. It is used for incrementing wanted set and user statistics.
 */
@RestController
@RequestMapping("/card-sets/{set_id}/set-statistics")
@RequiredArgsConstructor
public class SetStatisticsController {
    private final SetStatisticsServiceImpl setStatisticsService;

    private static final Logger logger = LogManager.getLogger(SetStatisticsController.class);

    @PatchMapping("/increment")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Increment wanted set statistics")
    public ResponseEntity<?> incrementSetStatistics(
            @PathVariable("set_id") UUID setId,
            @RequestParam String firstStat,
            @RequestParam(required = false) String secondStat,
            Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            setStatisticsService.incrementWantedStatistics(setId, userDetails.getId(), firstStat, secondStat);
            return ResponseEntity.ok("Wanted set statistic incremented");
        } catch (Exception e) {
            logger.error("Error incrementing set statistics: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
