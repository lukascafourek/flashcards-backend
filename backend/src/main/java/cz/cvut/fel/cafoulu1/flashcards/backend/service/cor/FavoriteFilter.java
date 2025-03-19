package cz.cvut.fel.cafoulu1.flashcards.backend.service.cor;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.springframework.data.jpa.domain.Specification;

public class FavoriteFilter implements CardSetFilter {
    @Override
    public Specification<CardSet> apply(FilterCardSetsRequest filterRequest, Specification<CardSet> spec) {
        if (Boolean.TRUE.equals(filterRequest.getFavorite())) {
            return spec.and(CardSetSpecification.isFavorite(filterRequest.getUserId()));
        }
        return spec;
    }
}
