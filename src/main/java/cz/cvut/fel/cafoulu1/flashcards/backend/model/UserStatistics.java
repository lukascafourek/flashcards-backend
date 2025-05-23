package cz.cvut.fel.cafoulu1.flashcards.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Represents global statistics of a user.
 */
@Entity
@Table(name = "user_statistics")
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class UserStatistics {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(unique = true, nullable = false, name = "user_id")
    @ToString.Exclude
    private User user;

    @Column(nullable = false, name = "total_sets_learned", columnDefinition = "integer default 0")
    private Integer totalSetsLearned = 0;

    @Column(nullable = false, name = "total_cards_learned", columnDefinition = "integer default 0")
    private Integer totalCardsLearned = 0;

    @Column(nullable = false, name = "total_cards_to_learn_again", columnDefinition = "integer default 0")
    private Integer totalCardsToLearnAgain = 0;

    @Column(nullable = false, name = "sets_created", columnDefinition = "integer default 0")
    private Integer setsCreated = 0;

    @Column(nullable = false, name = "cards_created", columnDefinition = "integer default 0")
    private Integer cardsCreated = 0;

    @Column(nullable = false, name = "base_method_modes", columnDefinition = "integer default 0")
    private Integer baseMethodModes = 0;

    @Column(nullable = false, name = "multiple_choice_modes", columnDefinition = "integer default 0")
    private Integer multipleChoiceModes = 0;

    @Column(nullable = false, name = "true_false_modes", columnDefinition = "integer default 0")
    private Integer trueFalseModes = 0;
}
