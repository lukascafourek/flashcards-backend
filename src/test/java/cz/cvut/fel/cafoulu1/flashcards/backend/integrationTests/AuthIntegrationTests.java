package cz.cvut.fel.cafoulu1.flashcards.backend.integrationTests;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.UpdateUserRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for core user authentication and account management functionality.
 * <p>
 * This test suite verifies that {@link UserServiceImpl} and {@link UserDetailsServiceImpl}
 * behave correctly when performing end-to-end operations involving the persistence layer.
 * The focus is on:
 * <ul>
 *     <li><b>User registration</b>: Creating a new user and persisting them to the database.</li>
 *     <li><b>User login</b>: Loading user details by email and verifying credentials.</li>
 *     <li><b>User update</b>: Modifying user data (username) while validating password checks.</li>
 *     <li><b>User deletion</b>: Removing a user and all related data from the database.</li>
 * </ul>
 *
 * <p><b>Test Scenario:</b></p>
 * <ol>
 *     <li>Register a new user with a unique email and password.</li>
 *     <li>Simulate a login by loading the user via {@link UserDetailsServiceImpl} and checking the password and roles.</li>
 *     <li>Update the registered user's username and assert the change in the database.</li>
 *     <li>Delete the user and verify that they no longer exist in the database.</li>
 * </ol>
 *
 * <p><b>Assumptions:</b></p>
 * <ul>
 *     <li>Tests are executed in the defined order using {@link Order} annotations.</li>
 *     <li>An in-memory or isolated test database is configured to avoid affecting production data.</li>
 *     <li>Passwords are encoded and decoded using the configured {@link PasswordEncoder} bean.</li>
 * </ul>
 *
 * <p>These tests ensure full-cycle integrity of the user account lifecycle in the application.</p>
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthIntegrationTests {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String EMAIL = "testuser@example.com";
    private static final String PASSWORD = "securePassword123";
    private static final String USERNAME = "InitialUser";
    private static final String UPDATED_USERNAME = "UpdatedUser";
    private static UUID userId;

    @Test
    @Order(1)
    public void registerUser_shouldPersistUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        request.setUsername(USERNAME);
        userService.registerUser(request);
        Optional<User> userOpt = userRepository.findByEmail(EMAIL);
        assertThat(userOpt).isPresent();
        User user = userOpt.get();
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        userId = user.getId();
    }

    @Test
    @Order(2)
    public void loginUser_shouldReturnUserDetails() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(EMAIL);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(EMAIL);
        assertTrue(passwordEncoder.matches(PASSWORD, userDetails.getPassword()));
        assertThat(userDetails.getAuthorities()).isNotEmpty();
    }

    @Test
    @Order(3)
    public void updateUser_shouldUpdateUsername() {
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setUsername(UPDATED_USERNAME);
        userService.updateUser(EMAIL, updateRequest);
        Optional<User> userOpt = userRepository.findByEmail(EMAIL);
        assertThat(userOpt).isPresent();
        assertThat(userOpt.get().getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Order(4)
    public void deleteUser_shouldRemoveUserFromRepository() {
        userService.deleteUser(userId);
        Optional<User> userOpt = userRepository.findById(userId);
        assertThat(userOpt).isEmpty();
    }
}
