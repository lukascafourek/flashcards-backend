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
@Getter
@Setter
@ToString
public class SetStatistics {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, name = "sets_learned", columnDefinition = "integer default 0")
    private Integer setsLearned = 0;

    @Column(nullable = false, name = "cards_learned", columnDefinition = "integer default 0")
    private Integer cardsLearned = 0;

    @Column(nullable = false, name = "cards_to_learn_again", columnDefinition = "integer default 0")
    private Integer cardsToLearnAgain = 0;

    @Column(nullable = false, name = "base_method_mode", columnDefinition = "integer default 0")
    private Integer baseMethodMode = 0;

    @Column(nullable = false, name = "multiple_choice_mode", columnDefinition = "integer default 0")
    private Integer multipleChoiceMode = 0;

    @Column(nullable = false, name = "true_false_mode", columnDefinition = "integer default 0")
    private Integer trueFalseMode = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "card_set_id")
    @ToString.Exclude
    private CardSet cardSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    @ToString.Exclude
    private User user;
}
