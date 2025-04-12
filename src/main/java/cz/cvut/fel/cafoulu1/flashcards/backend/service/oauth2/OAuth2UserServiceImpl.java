package cz.cvut.fel.cafoulu1.flashcards.backend.service.oauth2;

import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.UserMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.builder.UserBuilder;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserStatisticsRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.RegistrationEmailImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is used for custom OAuth2 user service.
 */
@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    private final UserStatisticsRepository userStatisticsRepository;

    private final UserMapper userMapper;

    private final RegistrationEmailImpl registrationEmail;

    private final UserBuilder userBuilder = new UserBuilder();

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(email, name));
        return new OAuth2UserImpl(userMapper.toDtoBasic(user), oAuth2User.getAttributes());
    }

    @Transactional
    protected User registerNewUser(String email, String name) {
        if (email == null || email.isEmpty() || email.length() > 255) {
            throw new IllegalArgumentException("Email is required (max 255 characters)");
        }
        if (name == null || name.isEmpty() || name.length() > 255) {
            throw new IllegalArgumentException("Name is required (max 255 characters)");
        }
        User user = userBuilder
                .setEmail(email)
                .setUsername(name)
                .setProvider(AuthProvider.GOOGLE)
                .build();
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setUser(user);
        userStatisticsRepository.save(userStatistics);
        User savedUser = userRepository.save(user);
        registrationEmail.sendEmail(name, email);
        return savedUser;
    }
}
