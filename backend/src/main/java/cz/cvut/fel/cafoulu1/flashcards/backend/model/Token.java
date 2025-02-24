package cz.cvut.fel.cafoulu1.flashcards.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a token used for password reset.
 */
@Entity
@Table(name = "tokens")
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Token {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(unique = true, nullable = false, name = "user_id")
    private User user;

    @Column(unique = true, nullable = false, name = "reset_token")
    private String resetToken;

    @Column(nullable = false, name = "expiration_date")
    private LocalDateTime expirationDate;
}
