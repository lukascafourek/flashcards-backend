package cz.cvut.fel.cafoulu1.flashcards.backend.service.cor;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter for card sets based on the creator.
 */
public class CreatorFilter implements CardSetFilter{
    @Override
    public Specification<CardSet> apply(FilterCardSetsRequest filterRequest, Specification<CardSet> spec) {
        if (filterRequest.getMine() != null && filterRequest.getMine() && filterRequest.getUserId() != null) {
            return spec.and(CardSetSpecification.hasCreator(filterRequest.getUserId()));
        }
        return spec;
    }
}
