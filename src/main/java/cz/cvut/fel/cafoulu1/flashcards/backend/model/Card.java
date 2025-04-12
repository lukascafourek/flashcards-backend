package cz.cvut.fel.cafoulu1.flashcards.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Represents a card in a card set.
 */
@Entity
@Table(name = "cards")
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Card {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, name = "front", columnDefinition = "VARCHAR(510)")
    private String front;

    @Column(nullable = false, name = "back", columnDefinition = "VARCHAR(510)")
    private String back;

    @Column(nullable = false, name = "card_order")
    private Integer cardOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "card_set_id")
    @ToString.Exclude
    private CardSet cardSet;
}
