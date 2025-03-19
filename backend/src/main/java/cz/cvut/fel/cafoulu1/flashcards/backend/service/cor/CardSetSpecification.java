package cz.cvut.fel.cafoulu1.flashcards.backend.service.cor;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CardSetSpecification {
    public static Specification<CardSet> hasCreator(UUID userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<CardSet> isFavorite(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) return criteriaBuilder.conjunction();
            Join<CardSet, User> userJoin = root.join("favoriteUsers");
            return criteriaBuilder.equal(userJoin.get("id"), userId);
        };
    }

    public static Specification<CardSet> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                (category == null || category.isEmpty()) ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<CardSet> containsText(String searchText) {
        return (root, query, criteriaBuilder) ->
                (searchText == null || searchText.isEmpty()) ? criteriaBuilder.conjunction() :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchText.toLowerCase() + "%");
    }
}
