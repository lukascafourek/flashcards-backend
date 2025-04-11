package cz.cvut.fel.cafoulu1.flashcards.backend.service.cor;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter for card sets based on category.
 */
public class CategoryFilter implements CardSetFilter {
    @Override
    public Specification<CardSet> apply(FilterCardSetsRequest filterRequest, Specification<CardSet> spec) {
        if (filterRequest.getCategory() != null && !filterRequest.getCategory().isEmpty()) {
            return spec.and(CardSetSpecification.hasCategory(filterRequest.getCategory()));
        }
        return spec;
    }
}
