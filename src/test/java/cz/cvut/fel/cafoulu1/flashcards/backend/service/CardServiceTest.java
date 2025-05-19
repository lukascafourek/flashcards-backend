package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.PictureMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private PictureRepository pictureRepository;
    @Mock
    private CardSetRepository cardSetRepository;
    @Mock
    private UserStatisticsRepository userStatisticsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private PictureMapper pictureMapper;
    @InjectMocks
    private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCard_createsCardSuccessfullyWhenUserIsOwner() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User user = new User();
        CardRequest cardRequest = new CardRequest();
        Card card = new Card();
        UserStatistics userStatistics = new UserStatistics();
        user.setId(userId);
        cardSet.setId(cardSetId);
        cardSet.setUser(user);
        cardSet.setCards(new ArrayList<>());
        card.setId(UUID.randomUUID());
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.of(userStatistics));
        when(cardMapper.createCard(cardRequest)).thenReturn(card);
        UUID result = cardService.createCard(cardSetId, cardRequest, userId);
        assertNotNull(result);
        assertEquals(1, userStatistics.getCardsCreated());
        assertTrue(cardSet.getCards().contains(card));
        verify(cardRepository).save(card);
        verify(userStatisticsRepository).save(userStatistics);
        verify(cardSetRepository).save(cardSet);
    }

    @Test
    void createCard_throwsExceptionWhenCardSetNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardRequest cardRequest = new CardRequest();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardService.createCard(cardSetId, cardRequest, userId));
        assertEquals("Card set not found", exception.getMessage());
        verifyNoInteractions(cardRepository, userStatisticsRepository);
    }

    @Test
    void createCard_throwsExceptionWhenUserIsNotOwner() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID());
        cardSet.setUser(anotherUser);
        CardRequest cardRequest = new CardRequest();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardService.createCard(cardSetId, cardRequest, userId));
        assertEquals("Card set does not belong to user", exception.getMessage());
        verifyNoInteractions(cardRepository, userStatisticsRepository);
    }

    @Test
    void createCard_throwsExceptionWhenUserStatisticsNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User user = new User();
        CardRequest cardRequest = new CardRequest();
        Card card = new Card();
        user.setId(userId);
        cardSet.setId(cardSetId);
        cardSet.setUser(user);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.empty());
        when(cardMapper.createCard(cardRequest)).thenReturn(card);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardService.createCard(cardSetId, cardRequest, userId));
        assertEquals("User statistics not found", exception.getMessage());
        verifyNoInteractions(cardRepository);
    }

    @Test
    void createCard_throwsExceptionWhenImageCountExceedsLimit() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User user = new User();
        CardRequest cardRequest = new CardRequest();
        cardRequest.setPicture(Base64.getEncoder().encodeToString(new byte[10]));
        user.setId(userId);
        user.setNumberOfImages(4);
        cardSet.setUser(user);
        cardSet.setCards(new ArrayList<>());
        Card card = new Card();
        card.setId(UUID.randomUUID());
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.of(new UserStatistics()));
        when(cardMapper.createCard(cardRequest)).thenReturn(card);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardService.createCard(cardSetId, cardRequest, userId));
        assertEquals("You have reached the maximum number of images on your account", exception.getMessage());
        verifyNoInteractions(pictureRepository);
    }

    @Test
    void createCard_throwsExceptionWhenImageSizeExceedsLimit() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User user = new User();
        CardRequest cardRequest = new CardRequest();
        cardRequest.setPicture(Base64.getEncoder().encodeToString(new byte[1024 * 1024 + 1]));
        user.setId(userId);
        user.setNumberOfImages(0);
        cardSet.setUser(user);
        cardSet.setCards(new ArrayList<>());
        Card card = new Card();
        card.setId(UUID.randomUUID());
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.of(new UserStatistics()));
        when(cardMapper.createCard(cardRequest)).thenReturn(card);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardService.createCard(cardSetId, cardRequest, userId));
        assertEquals("Image size exceeds the 1MB limit", exception.getMessage());
        verifyNoInteractions(pictureRepository);
    }

    @Test
    void getCards_throwsExceptionWhenCardSetNotFound() {
        UUID cardSetId = UUID.randomUUID();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardService.getCards(cardSetId));
        assertEquals("Card set not found", exception.getMessage());
        verify(cardSetRepository).findById(cardSetId);
        verifyNoInteractions(cardRepository);
    }

    @Test
    void validateIds_returnsCardWhenAllIdsAreValid() {
        UUID cardSetId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Card card = new Card();
        CardSet cardSet = new CardSet();
        User user = new User();
        user.setId(userId);
        user.setRole(Role.USER);
        cardSet.setId(cardSetId);
        cardSet.setUser(user);
        card.setId(cardId);
        card.setCardSet(cardSet);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Card result = cardService.validateIds(cardSetId, cardId, userId);
        assertEquals(card, result);
    }

    @Test
    void validateIds_throwsExceptionWhenCardNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardService.validateIds(cardSetId, cardId, userId));
        assertEquals("Card not found", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void validateIds_throwsExceptionWhenUserNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Card card = new Card();
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardService.validateIds(cardSetId, cardId, userId));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void validateIds_throwsExceptionWhenCardDoesNotBelongToUser() {
        UUID cardSetId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Card card = new Card();
        CardSet cardSet = new CardSet();
        User user = new User();
        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID());
        user.setId(userId);
        user.setRole(Role.USER);
        cardSet.setId(cardSetId);
        cardSet.setUser(anotherUser);
        card.setId(cardId);
        card.setCardSet(cardSet);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardService.validateIds(cardSetId, cardId, userId));
        assertEquals("Card does not belong to user", exception.getMessage());
    }

    @Test
    void validateIds_throwsExceptionWhenCardDoesNotBelongToCardSet() {
        UUID cardSetId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Card card = new Card();
        CardSet cardSet = new CardSet();
        CardSet anotherCardSet = new CardSet();
        User user = new User();
        user.setId(userId);
        anotherCardSet.setId(UUID.randomUUID());
        anotherCardSet.setUser(user);
        cardSet.setId(cardSetId);
        cardSet.setUser(user);
        card.setId(cardId);
        card.setCardSet(anotherCardSet);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardService.validateIds(cardSetId, cardId, userId));
        assertEquals("Card does not belong to card set", exception.getMessage());
    }

    @Test
    void validateIds_returnsCardWhenUserIsAdmin() {
        UUID cardSetId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Card card = new Card();
        CardSet cardSet = new CardSet();
        User admin = new User();
        admin.setId(userId);
        admin.setRole(Role.ADMIN);
        cardSet.setId(cardSetId);
        User user = new User();
        user.setId(UUID.randomUUID());
        cardSet.setUser(user);
        card.setId(cardId);
        card.setCardSet(cardSet);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(userRepository.findById(userId)).thenReturn(Optional.of(admin));
        Card result = cardService.validateIds(cardSetId, cardId, userId);
        assertEquals(card, result);
    }
}
