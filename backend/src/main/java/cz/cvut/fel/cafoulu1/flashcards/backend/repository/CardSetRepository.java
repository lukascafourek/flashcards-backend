package cz.cvut.fel.cafoulu1.flashcards.backend.repository;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for card sets.
 */
public interface CardSetRepository extends JpaRepository<CardSet, UUID> {
    Page<CardSet> findAll(Specification<CardSet> spec, Pageable pageable);
}