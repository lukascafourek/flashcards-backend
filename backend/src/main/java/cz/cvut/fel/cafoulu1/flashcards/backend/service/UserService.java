package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.UpdateUserRequest;

import java.util.UUID;

/**
 * This is a service for handling user requests.
 */
public interface UserService {
    void registerUser(RegisterRequest registerRequest);

    void updateUser(String email, UpdateUserRequest updateUserRequest);

//    void updateEmail(UUID userId, String newEmail);
//
//    void updateUsername(UUID userId, String newUsername);
//
//    void updatePassword(UUID userId, String newPassword);
//
//    void resetPassword(String email, String newPassword);

    void deleteUser(UUID userId);

    BasicUserDto findById(UUID userId);

//    boolean existsByEmail(String email);
//
//    boolean checkPassword(UUID userId, String password);
}
