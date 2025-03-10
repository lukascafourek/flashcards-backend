package cz.cvut.fel.cafoulu1.flashcards.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Represents a user of the application.
 */
@Entity
@Table(name = "users")
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(nullable = false, name = "username")
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "provider")
    private AuthProvider provider;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CardSet> cardSets;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<SetStatistics> setStatistics;

    @ManyToMany(mappedBy = "favoriteUsers", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CardSet> favoriteSets;
}
