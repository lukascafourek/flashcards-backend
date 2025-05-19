package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullUserInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

/**
 * This is a service for handling user requests.
 */
public interface UserService {
    /**
     * Registers a new user.
     *
     * @param registerRequest the request containing the details of the user to be registered
     */
    void registerUser(RegisterRequest registerRequest);

    /**
     * Updates the details of an existing user.
     *
     * @param email             the email of the user to be updated
     * @param updateUserRequest the request containing the updated details of the user
     */
    void updateUser(String email, UpdateUserRequest updateUserRequest);

    /**
     * Resets the password of a user.
     *
     * @param email    the email of the user whose password is to be reset
     * @param password the new password
     */
    void resetPassword(String email, String password);

    /**
     * Deletes a user.
     *
     * @param userId the ID of the user to be deleted
     */
    void deleteUser(UUID userId);

    /**
     * Gets the details of a user.
     *
     * @param userId the ID of the user to be retrieved
     * @return the details of the user
     */
    UserDto findById(UUID userId);

    /**
     * Gets a list of all users.
     *
     * @return a list of all users
     */
    List<FullUserInfo> findAll();
}
