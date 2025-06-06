package cz.cvut.fel.cafoulu1.flashcards.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Represents a picture associated with a card.
 */
@Entity
@Table(name = "pictures")
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Picture {
    @Id
    @Column(name = "card_id")
    private UUID cardId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(nullable = false, name = "card_id")
    @ToString.Exclude
    private Card card;

    @Column(nullable = false, columnDefinition = "bytea", name = "picture")
    private byte[] picture;
}
