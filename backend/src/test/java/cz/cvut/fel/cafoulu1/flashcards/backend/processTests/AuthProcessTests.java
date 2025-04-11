package cz.cvut.fel.cafoulu1.flashcards.backend.processTests;

import cz.cvut.fel.cafoulu1.flashcards.backend.controller.AuthController;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.LoginRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.UpdateUserRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Process tests that verify the full authentication lifecycle
 * using actual controller method calls. It simulates a real user performing
 * authentication-related actions in two sequential processes.
 *
 * <p><b>Test Scenario:</b></p>
 *
 * <ol>
 *     <li><b>Register, Update, Logout Process (@Order 1)</b>
 *         <ul>
 *             <li>Registers a new user using the <code>/auth/register</code> endpoint.</li>
 *             <li>Authenticates the user via <code>/auth/login</code> and retrieves the JWT token from the response cookie.</li>
 *             <li>Simulates an authenticated request by setting the SecurityContext
 *             and calls the <code>/auth/update-user</code> endpoint to update the username.</li>
 *             <li>Validates that the username was updated in the database.</li>
 *             <li>Calls the <code>/auth/logout</code> endpoint and verifies the logout response and cookie clearing.</li>
 *         </ul>
 *     </li>
 *     <li><b>Login, Delete Process (@Order 2)</b>
 *         <ul>
 *             <li>Logs the same user in again via <code>/auth/login</code>.</li>
 *             <li>Simulates an authenticated request and calls the <code>/auth/delete-account</code> endpoint.</li>
 *             <li>Confirms that the user is deleted from the database.</li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * <p>This test uses Spring's <code>@SpringBootTest</code> to boot up the context and simulate controller-level logic,
 * and uses <code>MockHttpServletResponse</code> to inspect response headers and cookies.</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthProcessTests {
    @Autowired
    private AuthController authController;
    @Autowired
    private UserRepository userRepository;
    private static final String EMAIL = "processtest@example.com";
    private static final String PASSWORD = "securePassword123";
    private static final String INITIAL_USERNAME = "ProcessUser";
    private static final String UPDATED_USERNAME = "UpdatedProcessUser";
    private static UUID userId;

    @Test
    @Order(1)
    void registerUpdateLogoutProcess() {
        // Register
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(EMAIL);
        registerRequest.setPassword(PASSWORD);
        registerRequest.setUsername(INITIAL_USERNAME);

        var registerResponse = authController.registerUser(registerRequest);
        assertThat(registerResponse.getStatusCode().is2xxSuccessful()).isTrue();

        Optional<User> userOpt = userRepository.findByEmail(EMAIL);
        assertThat(userOpt).isPresent();
        User user = userOpt.get();
        userId = user.getId();
        assertThat(user.getUsername()).isEqualTo(INITIAL_USERNAME);

        // Login to get token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(EMAIL);
        loginRequest.setPassword(PASSWORD);

        MockHttpServletResponse loginResponse = new MockHttpServletResponse();
        var loginResult = authController.loginUser(loginRequest, loginResponse);
        assertThat(loginResult.getStatusCode().is2xxSuccessful()).isTrue();

        String jwtCookie = loginResponse.getHeader("Set-Cookie");
        assertThat(jwtCookie).contains("jwt=");

        String jwtToken = extractTokenFromCookie(jwtCookie);
        assertThat(jwtToken).isNotBlank();

        // Update
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setUsername(UPDATED_USERNAME);

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var updateResult = authController.updateUser(updateRequest, authentication);
        assertThat(updateResult.getStatusCode().is2xxSuccessful()).isTrue();

        Optional<User> updatedUserOpt = userRepository.findByEmail(EMAIL);
        assertThat(updatedUserOpt).isPresent();
        assertThat(updatedUserOpt.get().getUsername()).isEqualTo(UPDATED_USERNAME);

        // Logout
        MockHttpServletResponse logoutResponse = new MockHttpServletResponse();
        var logoutResult = authController.logoutUser(logoutResponse);
        assertThat(logoutResult.getStatusCode().is2xxSuccessful()).isTrue();
        List<String> cookies = logoutResponse.getHeaders("Set-Cookie");
        assertThat(cookies).anyMatch(c -> c.startsWith("jwt=;"));
    }

    @Test
    @Order(2)
    void loginDeleteProcess() {
        // Login again
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(EMAIL);
        loginRequest.setPassword(PASSWORD);

        MockHttpServletResponse loginResponse = new MockHttpServletResponse();
        var loginResult = authController.loginUser(loginRequest, loginResponse);
        assertThat(loginResult.getStatusCode().is2xxSuccessful()).isTrue();

        Optional<User> userOpt = userRepository.findByEmail(EMAIL);
        assertThat(userOpt).isPresent();
        User user = userOpt.get();

        // Set auth context to call authenticated delete
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Delete
        MockHttpServletResponse deleteResponse = new MockHttpServletResponse();
        var deleteResult = authController.deleteAccount(authentication, deleteResponse);
        assertThat(deleteResult.getStatusCode().is2xxSuccessful()).isTrue();

        Optional<User> deletedUser = userRepository.findById(userId);
        assertThat(deletedUser).isEmpty();
    }

    private String extractTokenFromCookie(String cookieHeader) {
        for (String cookie : cookieHeader.split(";")) {
            if (cookie.trim().startsWith("jwt=")) {
                return cookie.trim().substring("jwt=".length());
            }
        }
        return null;
    }
}
