package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.PictureMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link CardService}.
 */
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    private final PictureRepository pictureRepository;

    private final CardSetRepository cardSetRepository;

    private final UserStatisticsRepository userStatisticsRepository;

    private final UserRepository userRepository;

    private final CardMapper cardMapper;

    private final PictureMapper pictureMapper;

    private final Tika tika = new Tika();

    private static final int MAX_IMAGE_SIZE = 1024 * 1024;

    private static final int MAX_IMAGE_COUNT_PER_USER = 1;

    @Transactional
    @Override
    public UUID createCard(UUID cardSetId, CardRequest cardRequest, UUID userId) {
        CardSet cardSet = cardSetRepository.findById(cardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Card set not found"));
        if (!cardSet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Card set does not belong to user");
        }

        Card card = cardMapper.createCard(cardRequest);
        card.setCardSet(cardSet);
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        userStatistics.setCardsCreated(userStatistics.getCardsCreated() + 1);
        userStatisticsRepository.save(userStatistics);
        cardRepository.save(card);
        if (cardRequest.getPicture() != null) {
            User user = cardSet.getUser();
            if (user.getNumberOfImages() >= MAX_IMAGE_COUNT_PER_USER) {
                throw new IllegalArgumentException("You have reached the maximum number of images on your account");
            }
            byte[] decodedPicture = Base64.getDecoder().decode(cardRequest.getPicture());
            if (decodedPicture.length > MAX_IMAGE_SIZE) {
                throw new IllegalArgumentException("Image size exceeds the 1MB limit");
            }
            Picture picture = pictureMapper.createPicture(decodedPicture, card);
            pictureRepository.save(picture);
            user.setNumberOfImages(user.getNumberOfImages() + 1);
            userRepository.save(user);
        }
        cardSet.getCards().add(card);
        cardSetRepository.save(cardSet);
        return card.getId();
    }

    @Transactional
    @Override
    public void updateCard(UUID cardSetId, UUID cardId, CardRequest cardRequest, UUID userId) {
        Card card = validateIds(cardSetId, cardId, userId);
        Card updatedCard = cardMapper.partialUpdateCard(cardRequest, card);
        if (cardRequest.getPicture() != null) {
            byte[] decodedPicture = Base64.getDecoder().decode(cardRequest.getPicture());
            if (decodedPicture.length > MAX_IMAGE_SIZE) {
                throw new IllegalArgumentException("Image size exceeds the 1MB limit");
            }
            pictureRepository.findById(cardId)
                    .ifPresentOrElse(picture -> {
                        picture.setPicture(decodedPicture);
                        pictureRepository.save(picture);
                    }, () -> {
                        User user = card.getCardSet().getUser();
                        if (user.getNumberOfImages() >= MAX_IMAGE_COUNT_PER_USER) {
                            throw new IllegalArgumentException("You have reached the maximum number of images on your account");
                        }
                        Picture newPicture = pictureMapper.createPicture(decodedPicture, updatedCard);
                        pictureRepository.save(newPicture);
                        user.setNumberOfImages(user.getNumberOfImages() + 1);
                        userRepository.save(user);
                    });
        } else {
            pictureRepository.findById(cardId).ifPresent(picture -> {
                User user = card.getCardSet().getUser();
                pictureRepository.delete(picture);
                user.setNumberOfImages(user.getNumberOfImages() - 1);
                userRepository.save(user);
            });
        }
        cardRepository.save(updatedCard);
    }

    @Transactional
    @Override
    public List<CardDto> getCards(UUID cardSetId) {
        CardSet cardSet = cardSetRepository.findById(cardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Card set not found"));
        return cardRepository.findByCardSet(cardSet).stream()
                .map(card -> {
                    CardDto cardDto = cardMapper.toDto(card);
                    pictureRepository.findById(card.getId()).ifPresent(picture -> {
                        byte[] imageData = picture.getPicture();
                        String mimeType = tika.detect(imageData);
                        String encodedPicture = Base64.getEncoder().encodeToString(imageData);
                        cardDto.setPicture(encodedPicture);
                        cardDto.setMimeType(mimeType);
                    });
                    return cardDto;
                })
                .toList();
    }

    @Transactional
    @Override
    public void deleteCard(UUID cardSetId, UUID cardId, UUID userId) {
        Card card = validateIds(cardSetId, cardId, userId);
        CardSet cardSet = card.getCardSet();
        pictureRepository.findById(card.getId()).ifPresent(picture -> {
            User user = cardSet.getUser();
            pictureRepository.delete(picture);
            user.setNumberOfImages(user.getNumberOfImages() - 1);
            userRepository.save(user);
        });
        cardSet.getCards().remove(card);
        cardSetRepository.save(cardSet);
        cardRepository.delete(card);
    }

    private Card validateIds(UUID cardSetId, UUID cardId, UUID userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        if (!card.getCardSet().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Card does not belong to user");
        }
        if (!card.getCardSet().getId().equals(cardSetId)) {
            throw new IllegalArgumentException("Card does not belong to card set");
        }
        return card;
    }
}
