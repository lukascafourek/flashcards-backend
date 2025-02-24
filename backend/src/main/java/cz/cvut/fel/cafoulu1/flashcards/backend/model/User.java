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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "username")
    private String username;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<CardSet> cardSets;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<SetStatistics> setStatistics;

    @ManyToMany(mappedBy = "users")
    @ToString.Exclude
    private List<CardSet> favoriteSets;
}
