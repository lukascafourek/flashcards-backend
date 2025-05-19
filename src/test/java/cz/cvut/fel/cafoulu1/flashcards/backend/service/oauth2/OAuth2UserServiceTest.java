package cz.cvut.fel.cafoulu1.flashcards.backend.service.oauth2;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserStatisticsRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.RegistrationEmailImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OAuth2UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatisticsRepository userStatisticsRepository;
    @Mock
    private RegistrationEmailImpl registrationEmail;
    @InjectMocks
    private OAuth2UserServiceImpl oAuth2UserServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerNewUser_throwsExceptionWhenEmailIsInvalid() {
        String invalidEmail = "";
        String name = "User Name";
        assertThrows(IllegalArgumentException.class, () -> oAuth2UserServiceImpl.registerNewUser(invalidEmail, name));
    }

    @Test
    void registerNewUser_throwsExceptionWhenNameIsInvalid() {
        String email = "user@example.com";
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> oAuth2UserServiceImpl.registerNewUser(email, invalidName));
    }

    @Test
    void registerNewUser_savesUserAndStatisticsWhenValid() {
        String email = "user@example.com";
        String name = "User Name";
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = oAuth2UserServiceImpl.registerNewUser(email, name);
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(userStatisticsRepository).save(any(UserStatistics.class));
        verify(registrationEmail).sendEmail(name, email);
    }
}
