package cz.cvut.fel.cafoulu1.flashcards.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a set of flashcards.
 */
@Entity
@Table(name = "card_sets")
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class CardSet {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "category")
    private Category category;

    @Column(nullable = false, name = "creation_date")
    private LocalDate creationDate;

    @Column(nullable = false, name = "privacy", columnDefinition = "boolean default true")
    private Boolean privacy = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "cardSet", fetch = FetchType.LAZY)
    @OrderColumn(name = "card_order")
    @ToString.Exclude
    private List<Card> cards = new ArrayList<>();

    @OneToMany(mappedBy = "cardSet", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<SetStatistics> setStatistics = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorite_sets",
            joinColumns = @JoinColumn(name = "card_set_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ToString.Exclude
    private List<User> favoriteUsers = new ArrayList<>();
}
