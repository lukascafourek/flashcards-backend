package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.CardServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for card management. It provides endpoints for creating, updating, deleting and getting cards.
 */
@RestController
@RequestMapping("/card-sets/{set_id}/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardServiceImpl cardService;

    private static final Logger logger = LogManager.getLogger(CardController.class);

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new card")
    public ResponseEntity<?> createCard(
            @PathVariable("set_id") UUID setId,
            @Valid @RequestBody CardRequest cardRequest,
            Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(cardService.createCard(setId, cardRequest, userDetails.getId()));
        } catch (Exception e) {
            logger.error("Error creating card: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update an existing card")
    public ResponseEntity<?> updateCard(
            @PathVariable("set_id") UUID setId,
            @PathVariable("id") UUID id,
            @Valid @RequestBody CardRequest cardRequest,
            Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            cardService.updateCard(setId, id, cardRequest, userDetails.getId());
            return ResponseEntity.ok("Card updated successfully");
        } catch (Exception e) {
            logger.error("Error updating card: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-cards")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all cards in a set")
    public ResponseEntity<?> getCards(@PathVariable("set_id") UUID setId) {
        try {
            return ResponseEntity.ok(cardService.getCards(setId));
        } catch (Exception e) {
            logger.error("Error getting cards: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete a card")
    public ResponseEntity<?> deleteCard(
            @PathVariable("set_id") UUID setId,
            @PathVariable("id") UUID id,
            Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            cardService.deleteCard(setId, id, userDetails.getId());
            return ResponseEntity.ok("Card deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting card: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
