package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullUserInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.LoginRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.UpdateUserRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.RegistrationEmailImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private UserServiceImpl userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RegistrationEmailImpl registrationEmail;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private AuthController authController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_returnsOkWhenUserIsRegisteredSuccessfully() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        registerRequest.setEmail("email@example.com");
        doNothing().when(userService).registerUser(registerRequest);
        doNothing().when(registrationEmail).sendEmail("username", "email@example.com");
        ResponseEntity<?> response = authController.registerUser(registerRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).registerUser(registerRequest);
        verify(registrationEmail).sendEmail("username", "email@example.com");
    }

    @Test
    void registerUser_returnsBadRequestWhenExceptionIsThrown() {
        RegisterRequest registerRequest = new RegisterRequest();
        doThrow(new RuntimeException("Testing error")).when(userService).registerUser(registerRequest);
        ResponseEntity<?> response = authController.registerUser(registerRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(userService).registerUser(registerRequest);
        verifyNoInteractions(registrationEmail);
    }

    @Test
    void loginUser_returnsBadRequestWhenExceptionIsThrown() {
        LoginRequest loginRequest = new LoginRequest();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Testing error"));
        ResponseEntity<?> result = authController.loginUser(loginRequest, response);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Testing error", result.getBody());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void getCurrentUser_returnsOkWhenUserIsRetrievedSuccessfully() {
        UUID userId = UUID.randomUUID();
        UserDto user = new UserDto();
        user.setEmail("email@example.com");
        user.setUsername("username");
        user.setProvider(AuthProvider.LOCAL);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(userService.findById(userId)).thenReturn(user);
        ResponseEntity<?> response = authController.getCurrentUser(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).findById(userId);
    }

    @Test
    void getCurrentUser_returnsBadRequestWhenExceptionIsThrown() {
        UUID userId = UUID.randomUUID();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(userService.findById(userId)).thenThrow(new RuntimeException("Testing error"));
        ResponseEntity<?> response = authController.getCurrentUser(authentication);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(userService).findById(userId);
    }

    @Test
    void getAllUsers_returnsOkWhenUsersAreRetrievedSuccessfully() {
        List<FullUserInfo> users = List.of(new FullUserInfo(), new FullUserInfo());
        when(userService.findAll()).thenReturn(users);
        ResponseEntity<?> response = authController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService).findAll();
    }

    @Test
    void getAllUsers_returnsBadRequestWhenExceptionIsThrown() {
        when(userService.findAll()).thenThrow(new RuntimeException("Testing error"));
        ResponseEntity<?> response = authController.getAllUsers();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(userService).findAll();
    }

    @Test
    void updateUser_returnsOkWhenUserIsUpdatedSuccessfully() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername("newUsername");
        updateUserRequest.setEmail("newEmail@example.com");
        updateUserRequest.setPassword("newPassword");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("email@example.com");
        doNothing().when(userService).updateUser("email@example.com", updateUserRequest);
        ResponseEntity<String> response = authController.updateUser(updateUserRequest, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).updateUser("email@example.com", updateUserRequest);
    }

    @Test
    void updateUser_returnsBadRequestWhenExceptionIsThrown() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("email@example.com");
        doThrow(new RuntimeException("Testing error")).when(userService).updateUser("email@example.com", updateUserRequest);
        ResponseEntity<String> response = authController.updateUser(updateUserRequest, authentication);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(userService).updateUser("email@example.com", updateUserRequest);
    }

    @Test
    void updateUserByAdmin_returnsOkWhenUserIsUpdatedSuccessfully() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername("newUsername");
        updateUserRequest.setEmail("newEmail@example.com");
        updateUserRequest.setPassword("newPassword");
        doNothing().when(userService).updateUser("email@example.com", updateUserRequest);
        ResponseEntity<String> response = authController.updateUserByAdmin("email@example.com", updateUserRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).updateUser("email@example.com", updateUserRequest);
    }

    @Test
    void updateUserByAdmin_returnsBadRequestWhenExceptionIsThrown() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        doThrow(new RuntimeException("Testing error")).when(userService).updateUser("email@example.com", updateUserRequest);
        ResponseEntity<String> response = authController.updateUserByAdmin("email@example.com", updateUserRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(userService).updateUser("email@example.com", updateUserRequest);
    }

    @Test
    void deleteAccount_returnsBadRequestWhenExceptionIsThrown() {
        UUID userId = UUID.randomUUID();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doThrow(new RuntimeException("Testing error")).when(userService).deleteUser(userId);
        ResponseEntity<String> result = authController.deleteAccount(authentication, response);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Testing error", result.getBody());
        verify(userService).deleteUser(userId);
    }

    @Test
    void deleteAccountByAdmin_returnsOkWhenAccountIsDeletedSuccessfully() {
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).deleteUser(userId);
        ResponseEntity<String> response = authController.deleteAccountByAdmin(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).deleteUser(userId);
    }

    @Test
    void deleteAccountByAdmin_returnsBadRequestWhenExceptionIsThrown() {
        UUID userId = UUID.randomUUID();
        doThrow(new RuntimeException("Testing error")).when(userService).deleteUser(userId);
        ResponseEntity<String> response = authController.deleteAccountByAdmin(userId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(userService).deleteUser(userId);
    }

    @Test
    void resetPassword_returnsOkWhenPasswordIsResetSuccessfully() {
        String email = "email@example.com";
        String password = "newPassword";
        doNothing().when(userService).resetPassword(eq(email), eq(password));
        ResponseEntity<?> response = authController.resetPassword(email, password);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).resetPassword(eq(email), eq(password));
    }

    @Test
    void resetPassword_returnsBadRequestWhenExceptionIsThrown() {
        String email = "email@example.com";
        String password = "newPassword";
        doThrow(new RuntimeException("Testing error")).when(userService).resetPassword(eq(email), eq(password));
        ResponseEntity<?> response = authController.resetPassword(email, password);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(userService).resetPassword(eq(email), eq(password));
    }
}
