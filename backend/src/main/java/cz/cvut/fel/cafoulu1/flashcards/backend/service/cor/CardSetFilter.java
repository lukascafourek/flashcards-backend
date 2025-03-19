package cz.cvut.fel.cafoulu1.flashcards.backend.service.cor;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.springframework.data.jpa.domain.Specification;

public interface CardSetFilter {
    Specification<CardSet> apply(FilterCardSetsRequest filterRequest, Specification<CardSet> spec);
}
