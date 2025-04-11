package cz.cvut.fel.cafoulu1.flashcards.backend.service.cor;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface for filtering card sets based on various criteria.
 * Implementations of this interface should provide specific filtering logic.
 */
public interface CardSetFilter {
    /**
     * Applies the filter to the given specification based on the provided filter request.
     *
     * @param filterRequest the filter request containing the criteria for filtering
     * @param spec          the current specification to which the filter will be applied
     * @return the updated specification with the applied filter
     */
    Specification<CardSet> apply(FilterCardSetsRequest filterRequest, Specification<CardSet> spec);
}
