package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.LoginRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.security.jwtconfig.JwtUtils;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserService;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_shouldReturnBasicUserDtoWhenSuccessful() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test@example.com", "testuser", "password");

        BasicUserDto userDto = new BasicUserDto(UUID.randomUUID(), "test@example.com", "testuser", AuthProvider.LOCAL);
        when(userService.registerUser(registerRequest)).thenReturn(userDto);

        ResponseEntity<?> response = authController.registerUser(registerRequest, mock(HttpServletRequest.class));

        assertEquals(ResponseEntity.ok(userDto), response);
    }

    @Test
    void loginUser_shouldReturnJwtResponseWhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");

        ResponseEntity<?> response = authController.loginUser(loginRequest);
        int statusCode = response.getStatusCode().value();

        assertEquals(200, statusCode);
    }

    @Test
    void getCurrentUser_shouldReturnBasicUserDtoWhenAuthenticated() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(UUID.randomUUID());
        BasicUserDto userDto = new BasicUserDto(UUID.randomUUID(), "test@example.com", "testuser", AuthProvider.LOCAL);
        when(userService.findById(userDetails.getId())).thenReturn(userDto);

        ResponseEntity<BasicUserDto> response = authController.getCurrentUser(authentication);

        assertEquals(ResponseEntity.ok(userDto), response);
    }

    @Test
    void updateEmail_shouldReturnSuccessMessageWhenEmailUpdated() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(UUID.randomUUID());

        ResponseEntity<String> response = authController.updateEmail("newemail@example.com", authentication);

        assertEquals(ResponseEntity.ok("Email updated successfully."), response);
    }

    @Test
    void updateUsername_shouldReturnSuccessMessageWhenUsernameUpdated() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(UUID.randomUUID());

        ResponseEntity<String> response = authController.updateUsername("newusername", authentication);

        assertEquals(ResponseEntity.ok("Username updated successfully."), response);
    }

    @Test
    void updatePassword_shouldReturnSuccessMessageWhenPasswordUpdated() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(UUID.randomUUID());

        ResponseEntity<String> response = authController.updatePassword("newpassword", authentication);

        assertEquals(ResponseEntity.ok("Password updated successfully."), response);
    }

    @Test
    void deleteAccount_shouldReturnSuccessMessageWhenAccountDeleted() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(UUID.randomUUID());

        ResponseEntity<String> response = authController.deleteAccount(authentication);

        assertEquals(ResponseEntity.ok("Account deleted successfully."), response);
    }
}
