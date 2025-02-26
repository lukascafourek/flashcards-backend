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

    @Column(nullable = false, name = "total_sets_learned")
    private Integer totalSetsLearned;

    @Column(nullable = false, name = "total_cards_learned")
    private Integer totalCardsLearned;

    @Column(nullable = false, name = "total_cards_to_learn_again")
    private Integer totalCardsToLearnAgain;

    @Column(nullable = false, name = "sets_created")
    private Integer setsCreated;

    @Column(nullable = false, name = "cards_created")
    private Integer cardsCreated;

    @Column(nullable = false, name = "base_method_modes")
    private Integer baseMethodModes;

    @Column(nullable = false, name = "multiple_choice_modes")
    private Integer multipleChoiceModes;

    @Column(nullable = false, name = "connection_modes")
    private Integer connectionModes;
}
