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
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false, name = "front")
    private String front;

    @Column(nullable = false, name = "back")
    private String back;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "card_set_id")
    @ToString.Exclude
    private CardSet cardSet;
}
