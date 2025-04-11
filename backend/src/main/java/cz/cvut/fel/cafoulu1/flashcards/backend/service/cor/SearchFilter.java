package cz.cvut.fel.cafoulu1.flashcards.backend.service.cor;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter for card sets based on the search text.
 */
public class SearchFilter implements CardSetFilter {
    @Override
    public Specification<CardSet> apply(FilterCardSetsRequest filterRequest, Specification<CardSet> spec) {
        if (filterRequest.getSearch() != null && !filterRequest.getSearch().isEmpty()) {
            return spec.and(CardSetSpecification.containsText(filterRequest.getSearch()));
        }
        return spec;
    }
}
