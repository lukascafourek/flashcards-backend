package cz.cvut.fel.cafoulu1.flashcards.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Represents statistics of a specific user in a specific set.
 */
@Entity
@Table(name = "set_statistics")
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SetStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false, name = "sets_learned")
    private Integer setsLearned;

    @Column(nullable = false, name = "cards_learned")
    private Integer cardsLearned;

    @Column(nullable = false, name = "cards_to_learn_again")
    private Integer cardsToLearnAgain;

    @Column(nullable = false, name = "base_method_mode")
    private Integer baseMethodMode;

    @Column(nullable = false, name = "multiple_choice_mode")
    private Integer multipleChoiceMode;

    @Column(nullable = false, name = "connection_mode")
    private Integer connectionMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "card_set_id")
    @ToString.Exclude
    private CardSet cardSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    @ToString.Exclude
    private User user;
}
