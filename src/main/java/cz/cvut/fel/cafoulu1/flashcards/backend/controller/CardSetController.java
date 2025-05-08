package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardSetRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardSetMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.CardSetServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller for card sets management. It provides endpoints for creating, updating, deleting and getting card sets.
 */
@RestController
@RequestMapping("/card-sets")
@RequiredArgsConstructor
public class CardSetController {
    private final CardSetServiceImpl cardSetService;

    private final CardSetMapper cardSetMapper;

    private static final Logger logger = LogManager.getLogger(CardSetController.class);

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new card set")
    public ResponseEntity<?> createCardSet(@Valid @RequestBody CardSetRequest cardSetRequest, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(cardSetService.createCardSet(userDetails.getId(), cardSetRequest));
        } catch (Exception e) {
            logger.error("Error creating card set: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/copy/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Copy an existing card set")
    public ResponseEntity<?> copyCardSet(@PathVariable("id") UUID id, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(cardSetService.copyCardSet(id, userDetails.getId()));
        } catch (Exception e) {
            logger.error("Error copying card set: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update an existing card set")
    public ResponseEntity<?> updateCardSet(@PathVariable("id") UUID id, @Valid @RequestBody CardSetRequest cardSetRequest, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            cardSetService.updateCardSet(id, cardSetRequest, userDetails.getId());
            return ResponseEntity.ok("Card set updated");
        } catch (Exception e) {
            logger.error("Error updating card set: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/update-favorite/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update favorite status of a card set")
    public ResponseEntity<?> updateFavoriteCardSet(@PathVariable("id") UUID id, @RequestParam Boolean isFavorite, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            cardSetService.updateFavoriteCardSet(id, isFavorite, userDetails.getId());
            return ResponseEntity.ok("Card set updated");
        } catch (Exception e) {
            logger.error("Error updating favorite card set: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete a card set")
    public ResponseEntity<?> deleteCardSet(@PathVariable("id") UUID id, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            cardSetService.deleteCardSet(id, userDetails.getId());
            return ResponseEntity.ok("Card set deleted");
        } catch (Exception e) {
            logger.error("Error deleting card set: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get a card set by ID")
    public ResponseEntity<?> getCardSet(@PathVariable("id") UUID id, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(cardSetService.getCardSet(id, userDetails.getId()));
        } catch (Exception e) {
            logger.error("Error getting card set: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-sets")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all card sets with pagination and filtering")
    public ResponseEntity<?> getAllCardSetsWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "") @Size(max = 255) String category,
            @RequestParam(defaultValue = "") @Size(max = 255) String search,
            @RequestParam(defaultValue = "false") Boolean mySets,
            @RequestParam(defaultValue = "false") Boolean myFavorites,
            Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            FilterCardSetsRequest filterCardSetsRequest = cardSetMapper
                    .createFilterCardSetsRequest(category, search, mySets, myFavorites, userDetails.getId());
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(order), sortBy));
            return ResponseEntity.ok(cardSetService.getFilteredCardSets(pageable, filterCardSetsRequest));
        } catch (Exception e) {
            logger.error("Error getting card sets: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN') && isAuthenticated()")
    @Operation(summary = "Get all card sets")
    public ResponseEntity<?> getAllCardSets() {
        try {
            return ResponseEntity.ok(cardSetService.getAllCardSets());
        } catch (Exception e) {
            logger.error("Error getting all card sets: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-cards")
    @PreAuthorize("hasRole('ADMIN') && isAuthenticated()")
    @Operation(summary = "Get all cards")
    public ResponseEntity<?> getAllCards() {
        try {
            return ResponseEntity.ok(cardSetService.getAllCards());
        } catch (Exception e) {
            logger.error("Error getting all cards: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/update-card-order")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update the order of cards in a card set")
    public ResponseEntity<?> updateCardOrder(@PathVariable("id") UUID id, @RequestBody Map<String, List<CardDto>> orderList, Authentication authentication) {
        try {
            List<CardDto> cards = orderList.get("cards");
            if (cards == null || cards.isEmpty()) {
                throw new IllegalArgumentException("Cards list is required");
            }
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            cardSetService.updateOrderOfCards(id, cards, userDetails.getId());
            return ResponseEntity.ok("Card order updated");
        } catch (Exception e) {
            logger.error("Error updating card order: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
