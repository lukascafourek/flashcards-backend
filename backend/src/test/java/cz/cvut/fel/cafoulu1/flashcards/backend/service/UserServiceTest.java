package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.UserMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_shouldThrowExceptionWhenEmailIsInUse() {
        RegisterRequest request = new RegisterRequest("test@example.com", "testuser", "password");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }

    @Test
    void registerUser_shouldReturnBasicUserDtoWhenSuccessful() {
        RegisterRequest request = new RegisterRequest("test@example.com", "testuser", "password");

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword("encodedPassword");
        user.setProvider(AuthProvider.LOCAL);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDtoBasic(user)).thenReturn(new BasicUserDto(UUID.randomUUID(), "test@example.com", "testuser", AuthProvider.LOCAL));

        BasicUserDto result = userService.registerUser(request);

        assertNotNull(result);
    }

    @Test
    void updateEmail_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateEmail(userId, "newemail@example.com"));
    }

    @Test
    void updateEmail_shouldUpdateEmailWhenUserFound() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("oldemail@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.updateEmail(userId, "newemail@example.com");

        assertEquals("newemail@example.com", user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void updateUsername_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUsername(userId, "newusername"));
    }

    @Test
    void updateUsername_shouldUpdateUsernameWhenUserFound() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setUsername("oldusername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.updateUsername(userId, "newusername");

        assertEquals("newusername", user.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void updatePassword_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(userId, "newpassword"));
    }

    @Test
    void updatePassword_shouldUpdatePasswordWhenUserFound() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setPassword("oldpassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");

        userService.updatePassword(userId, "newpassword");

        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void deleteUser_shouldDeleteUserWhenUserFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void findById_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.findById(userId));
    }

    @Test
    void findById_shouldReturnBasicUserDtoWhenUserFound() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDtoBasic(user)).thenReturn(new BasicUserDto(UUID.randomUUID(), "test@example.com", "testuser", AuthProvider.LOCAL));

        BasicUserDto result = userService.findById(userId);

        assertNotNull(result);
    }
}
