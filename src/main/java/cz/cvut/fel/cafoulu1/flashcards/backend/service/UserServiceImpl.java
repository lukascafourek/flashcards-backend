package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullUserInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.RegisterRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.UpdateUserRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.UserMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.builder.UserBuilder;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.helper.CardSetDeletionHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
    * Implementation of {@link UserService}.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final CardSetRepository cardSetRepository;

    private final UserStatisticsRepository userStatisticsRepository;

    private final SetStatisticsRepository setStatisticsRepository;

    private final CardRepository cardRepository;

    private final PictureRepository pictureRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final UserBuilder userBuilder = new UserBuilder();

    @Transactional
    @Override
    public void registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        User user = userBuilder
                .setEmail(registerRequest.getEmail())
                .setPassword(passwordEncoder.encode(registerRequest.getPassword()))
                .setUsername(registerRequest.getUsername())
                .setProvider(AuthProvider.LOCAL)
                .build();
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setUser(user);
        userRepository.save(user);
        userStatisticsRepository.save(userStatistics);
    }

    @Transactional
    @Override
    public void updateUser(String email, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getProvider().equals(AuthProvider.GOOGLE) && (updateUserRequest.getEmail() != null || updateUserRequest.getPassword() != null)) {
            throw new IllegalArgumentException("You cannot change your email or password when registered with Google.");
        }
        if (updateUserRequest.getEmail() != null || updateUserRequest.getPassword() != null) {
            if (updateUserRequest.getCheck() == null || updateUserRequest.getCheck().isEmpty()) {
                throw new IllegalArgumentException("You must provide your current password to change your email or password.");
            }
        }
        if (updateUserRequest.getCheck() != null && !passwordEncoder.matches(updateUserRequest.getCheck(), user.getPassword())) {
            throw new IllegalArgumentException("Your current password is invalid.");
        }
        if (updateUserRequest.getEmail() != null && userRepository.existsByEmail(updateUserRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (updateUserRequest.getUsername() != null && !updateUserRequest.getUsername().isEmpty()) {
            if (updateUserRequest.getUsername().length() < 3) {
                throw new IllegalArgumentException("Username must be at least 3 characters long.");
            }
            user.setUsername(updateUserRequest.getUsername());
        }
        if (updateUserRequest.getEmail() != null && !updateUserRequest.getEmail().isEmpty()) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isEmpty()) {
            if (updateUserRequest.getPassword().length() < 8) {
                throw new IllegalArgumentException("Password must be at least 8 characters long.");
            }
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (newPassword != null && !newPassword.isEmpty()) {
            if (newPassword.length() < 8) {
                throw new IllegalArgumentException("Password must be at least 8 characters long.");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.getFavoriteSets().forEach(favoriteSet -> favoriteSet.getFavoriteUsers().remove(user));
        cardSetRepository.saveAll(user.getFavoriteSets());
        user.getCardSets().forEach((cardSet) -> CardSetDeletionHelper.getInstance()
                        .cardSetDeleteHelper(cardSet, userRepository, setStatisticsRepository, pictureRepository, cardRepository));
        cardSetRepository.deleteAll(user.getCardSets());
        List<CardSet> setStatisticsCardSets = user.getSetStatistics().stream()
                .map(setStatistics -> {
                    CardSet cardSet = setStatistics.getCardSet();
                    cardSet.getSetStatistics().remove(setStatistics);
                    return cardSet;
                })
                .toList();
        if (!setStatisticsCardSets.isEmpty()) {
            cardSetRepository.saveAll(setStatisticsCardSets);
            setStatisticsRepository.deleteAll(user.getSetStatistics());
        }
        userStatisticsRepository.deleteById(userId);
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public UserDto findById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public List<FullUserInfo> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toFullDto)
                .toList();
    }
}
