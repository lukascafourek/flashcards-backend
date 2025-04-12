package cz.cvut.fel.cafoulu1.flashcards.backend.service.cor;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter for card sets based on the favorite status.
 */
public class FavoriteFilter implements CardSetFilter {
    @Override
    public Specification<CardSet> apply(FilterCardSetsRequest filterRequest, Specification<CardSet> spec) {
        if (filterRequest.getFavorite() != null && filterRequest.getFavorite() && filterRequest.getUserId() != null) {
            return spec.and(CardSetSpecification.isFavorite(filterRequest.getUserId()));
        }
        return spec;
    }
}
