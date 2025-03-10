package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.LoginRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.security.jwtconfig.JwtUtils;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserService;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
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
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setUsername("testuser");

        HttpServletRequest request = mock(HttpServletRequest.class);
        BasicUserDto userDto = new BasicUserDto(UUID.randomUUID(), "test@example.com", "testuser", AuthProvider.LOCAL);
        when(userService.registerUser(registerRequest)).thenReturn(userDto);

        ResponseEntity<?> response = authController.registerUser(registerRequest, request);

        assertEquals(ResponseEntity.ok(userDto), response);
    }

    @Test
    void loginUser_shouldReturnJwtResponseWhenSuccessful() throws UnsupportedEncodingException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");

        ResponseEntity<?> result = authController.loginUser(loginRequest, response);
        int statusCode = result.getStatusCode().value();

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
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(UUID.randomUUID());

        ResponseEntity<String> result = authController.deleteAccount(authentication, response);

        assertEquals(ResponseEntity.ok("Account deleted successfully."), result);
        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), anyString());
    }

    @Test
    void emailExists_shouldReturnTrueWhenEmailExists() {
        String email = "test@example.com";
        when(userService.existsByEmail(email)).thenReturn(true);

        ResponseEntity<Boolean> response = authController.emailExists(email);

        assertEquals(ResponseEntity.ok(true), response);
    }

    @Test
    void emailExists_shouldReturnFalseWhenEmailDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userService.existsByEmail(email)).thenReturn(false);

        ResponseEntity<Boolean> response = authController.emailExists(email);

        assertEquals(ResponseEntity.ok(false), response);
    }

    @Test
    void checkPassword_shouldReturnTrueWhenPasswordMatches() {
        String password = "password";
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(UUID.randomUUID());
        when(userService.checkPassword(userDetails.getId(), password)).thenReturn(true);

        ResponseEntity<Boolean> response = authController.checkPassword(password, authentication);

        assertEquals(ResponseEntity.ok(true), response);
    }

    @Test
    void checkPassword_shouldReturnFalseWhenPasswordDoesNotMatch() {
        String password = "wrongPassword";
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(UUID.randomUUID());
        when(userService.checkPassword(userDetails.getId(), password)).thenReturn(false);

        ResponseEntity<Boolean> response = authController.checkPassword(password, authentication);

        assertEquals(ResponseEntity.ok(false), response);
    }

    @Test
    void checkPassword_shouldThrowExceptionWhenUserNotFound() {
        String password = "password";
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(UUID.randomUUID());
        when(userService.checkPassword(userDetails.getId(), password)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> authController.checkPassword(password, authentication));
    }

    @Test
    void logoutUser_shouldReturnSuccessMessageWhenUserLoggedOut() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        ResponseEntity<?> result = authController.logoutUser(response);

        assertEquals(ResponseEntity.ok("User logged out successfully."), result);
        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), anyString());
    }

    @Test
    void logoutUser_shouldSetJwtCookieToExpireImmediately() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        authController.logoutUser(response);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), captor.capture());
        String cookie = captor.getValue();
        assertTrue(cookie.contains("jwt="));
        assertTrue(cookie.contains("Max-Age=0"));
    }
}
