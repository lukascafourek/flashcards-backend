package cz.cvut.fel.cafoulu1.flashcards.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.ArrayList;
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
    @JsonIgnore
    @ToStringExclude
    private String password;

    @Column(nullable = false, name = "username")
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role", columnDefinition = "varchar(255) default 'USER'")
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "provider")
    private AuthProvider provider;

    @Column(nullable = false, name = "number_of_images", columnDefinition = "integer default 0")
    private Integer numberOfImages = 0;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CardSet> cardSets = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<SetStatistics> setStatistics = new ArrayList<>();

    @ManyToMany(mappedBy = "favoriteUsers", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CardSet> favoriteSets = new ArrayList<>();
}
