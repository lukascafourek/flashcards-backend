package cz.cvut.fel.cafoulu1.flashcards.backend.service.cor;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filter for card sets that are either public or owned by the user.
 */
public class PublicOrOwnedFilter implements CardSetFilter{
    @Override
    public Specification<CardSet> apply(FilterCardSetsRequest filterRequest, Specification<CardSet> spec) {
        if (filterRequest.getUserId() != null) {
            return spec.and(CardSetSpecification.isPublicOrOwnedByUser(filterRequest.getUserId()));
        }
        return spec;
    }
}
