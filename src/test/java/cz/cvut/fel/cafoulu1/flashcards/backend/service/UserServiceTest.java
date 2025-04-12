package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.UpdateUserRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.UserMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardSetRepository cardSetRepository;
    @Mock
    private UserStatisticsRepository userStatisticsRepository;
    @Mock
    private SetStatisticsRepository setStatisticsRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private PictureRepository pictureRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_shouldRegisterNewUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setUsername("testuser");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        userService.registerUser(request);

        verify(userRepository).save(any(User.class));
        verify(userStatisticsRepository).save(any(UserStatistics.class));
    }

    @Test
    void registerUser_shouldThrowIfEmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }

    @Test
    void updateUser_shouldUpdateUsernameAndPassword() {
        User user = new User();
        user.setPassword("encodedOldPassword");
        user.setProvider(AuthProvider.LOCAL);

        UpdateUserRequest update = new UpdateUserRequest();
        update.setUsername("newUser");
        update.setPassword("newPassword");
        update.setCheck("oldPassword");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.updateUser("user@example.com", update);

        assertEquals("newUser", user.getUsername());
        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_shouldThrowIfWrongPassword() {
        User user = new User();
        user.setPassword("encodedOldPassword");
        user.setProvider(AuthProvider.LOCAL);

        UpdateUserRequest update = new UpdateUserRequest();
        update.setPassword("newPassword");
        update.setCheck("wrongPassword");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedOldPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser("user@example.com", update));
    }

    @Test
    void deleteUser_shouldRemoveUserAndRelatedData() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setCardSets(List.of());
        user.setSetStatistics(List.of());
        user.setFavoriteSets(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userStatisticsRepository).deleteById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    void findById_shouldReturnUser() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        BasicUserDto dto = new BasicUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDtoBasic(user)).thenReturn(dto);

        BasicUserDto result = userService.findById(userId);
        assertEquals(dto, result);
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        List<User> users = List.of(new User(), new User());
        List<UserDto> dtos = List.of(new UserDto(), new UserDto());

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(any(User.class))).thenReturn(new UserDto());

        List<UserDto> result = userService.findAll();
        assertEquals(2, result.size());
    }
}
