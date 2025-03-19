package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardSetRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.CardSetServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for card sets management. It provides endpoints for creating, updating, deleting and getting card sets.
 */
@RestController
@RequestMapping("/card-sets")
@RequiredArgsConstructor
public class CardSetController {
    private final CardSetServiceImpl cardSetService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createCardSet(@RequestBody CardSetRequest cardSetRequest, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            cardSetService.createCardSet(userDetails.getId(), cardSetRequest);
            return ResponseEntity.ok("Card set created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateCardSet(@PathVariable("id") UUID id, @RequestBody CardSetRequest cardSetRequest) {
        try {
            cardSetService.updateCardSet(id, cardSetRequest);
            return ResponseEntity.ok("Card set updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteCardSet(@PathVariable("id") UUID id) {
        try {
            cardSetService.deleteCardSet(id);
            return ResponseEntity.ok("Card set deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCardSet(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(cardSetService.getCardSet(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-all-with-pagination")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllCardSetsWithPagination(
            @RequestBody FilterCardSetsRequest filterCardSetsRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy, order));
            return ResponseEntity.ok(cardSetService.getFilteredCardSets(pageable, filterCardSetsRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
