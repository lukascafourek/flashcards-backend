package cz.cvut.fel.cafoulu1.flashcards.backend.service.oauth2;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * This class is used for custom OAuth2 user service.
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(email, name));
        if (!user.getProvider().equals(AuthProvider.GOOGLE)) {
            user.setProvider(AuthProvider.GOOGLE);
            userRepository.save(user);
        }
        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(String email, String name) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(name);
        newUser.setProvider(AuthProvider.GOOGLE);
        return userRepository.save(newUser);
    }
}
