package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
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
        userStatisticsRepository.save(userStatistics);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(String email, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (updateUserRequest.getCheck() != null && !passwordEncoder.matches(updateUserRequest.getCheck(), user.getPassword())) {
            throw new IllegalArgumentException("Your current password is invalid.");
        }
        if (updateUserRequest.getEmail() != null && userRepository.existsByEmail(updateUserRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        User updatedUser = userMapper.partialUpdateUser(updateUserRequest, user);
        userRepository.save(updatedUser);
    }

//    @Override
//    public void updateEmail(UUID userId, String newEmail) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        user.setEmail(newEmail);
//        userRepository.save(user);
//    }
//
//    @Override
//    public void updateUsername(UUID userId, String newUsername) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        user.setUsername(newUsername);
//        userRepository.save(user);
//    }
//
//    @Override
//    public void updatePassword(UUID userId, String newPassword) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        user.setPassword(passwordEncoder.encode(newPassword));
//        userRepository.save(user);
//    }
//
//    @Override
//    public void resetPassword(String email, String newPassword) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        user.setPassword(passwordEncoder.encode(newPassword));
//        userRepository.save(user);
//    }

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

    @Override
    public BasicUserDto findById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toDtoBasic(user);
    }

//    @Override
//    public boolean existsByEmail(String email) {
//        return userRepository.existsByEmail(email);
//    }
//
//    @Override
//    public boolean checkPassword(UUID userId, String password) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        return passwordEncoder.matches(password, user.getPassword());
//    }
}
