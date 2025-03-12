package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;

import java.util.UUID;

/**
 * This is a service for handling user requests.
 */
public interface UserService {
    BasicUserDto registerUser(RegisterRequest registerRequest);

    void updateEmail(UUID userId, String newEmail);

    void updateUsername(UUID userId, String newUsername);

    void updatePassword(UUID userId, String newPassword);

    void deleteUser(UUID userId);

    BasicUserDto findById(UUID userId);

    BasicUserDto findByEmail(String email);

    boolean existsByEmail(String email);

    boolean checkPassword(UUID userId, String password);
}
