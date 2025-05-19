package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardSetRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardSetInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardSetMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.SetStatisticsMapper;
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

public class CardSetServiceTest {
    @Mock
    private CardSetRepository cardSetRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private SetStatisticsRepository setStatisticsRepository;
    @Mock
    private UserStatisticsRepository userStatisticsRepository;
    @Mock
    private CardSetMapper cardSetMapper;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private SetStatisticsMapper setStatisticsMapper;
    @InjectMocks
    private CardSetServiceImpl cardSetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCardSet_throwsExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        CardSetRequest cardSetRequest = new CardSetRequest();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.createCardSet(userId, cardSetRequest));
        assertEquals("User not found", exception.getMessage());
        verifyNoInteractions(userStatisticsRepository, cardSetMapper);
    }

    @Test
    void createCardSet_throwsExceptionWhenUserStatisticsNotFound() {
        CardSetRequest cardSetRequest = new CardSetRequest();
        User user = new User();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userStatisticsRepository.findById(user.getId())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.createCardSet(user.getId(), cardSetRequest));
        assertEquals("User statistics not found", exception.getMessage());
    }

    @Test
    void saveCardSetRelations_savesAllEntitiesCorrectly() {
        User user = new User();
        CardSet cardSet = new CardSet();
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setSetsCreated(0);
        when(userRepository.save(user)).thenReturn(user);
        when(cardSetRepository.save(cardSet)).thenReturn(cardSet);
        when(userStatisticsRepository.save(userStatistics)).thenReturn(userStatistics);
        UUID result = cardSetService.saveCardSetRelations(user, cardSet, userStatistics);
        assertEquals(cardSet.getId(), result);
        assertEquals(1, userStatistics.getSetsCreated());
        verify(userRepository).save(user);
        verify(cardSetRepository).save(cardSet);
        verify(userStatisticsRepository).save(userStatistics);
    }

    @Test
    void copyCardSet_throwsExceptionWhenCardSetNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.copyCardSet(cardSetId, userId));
        assertEquals("Card set not found", exception.getMessage());
        verifyNoInteractions(userRepository, userStatisticsRepository, cardSetMapper, cardMapper);
    }

    @Test
    void copyCardSet_throwsExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        CardSet toCopy = new CardSet();
        when(cardSetRepository.findById(toCopy.getId())).thenReturn(Optional.of(toCopy));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.copyCardSet(toCopy.getId(), userId));
        assertEquals("User not found", exception.getMessage());
        verifyNoInteractions(userStatisticsRepository, cardSetMapper, cardMapper);
    }

    @Test
    void copyCardSet_throwsExceptionWhenUserIsCreator() {
        UUID userId = UUID.randomUUID();
        CardSet toCopy = new CardSet();
        User user = new User();
        user.setId(userId);
        toCopy.setUser(user);
        when(cardSetRepository.findById(toCopy.getId())).thenReturn(Optional.of(toCopy));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.copyCardSet(toCopy.getId(), userId));
        assertEquals("User is the creator of the card set", exception.getMessage());
        verifyNoInteractions(userStatisticsRepository, cardSetMapper, cardMapper);
    }

    @Test
    void copyCardSet_throwsExceptionWhenCardSetIsPrivate() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet toCopy = new CardSet();
        User user = new User();
        UUID creatorId = UUID.randomUUID();
        User creator = new User();
        creator.setId(creatorId);
        toCopy.setUser(creator);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(toCopy));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(creatorId)).thenReturn(Optional.of(creator));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.copyCardSet(cardSetId, userId));
        assertEquals("Card set is private", exception.getMessage());
        verifyNoInteractions(userStatisticsRepository, cardSetMapper, cardMapper);
    }

    @Test
    void copyCardSet_throwsExceptionWhenUserStatisticsNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet toCopy = new CardSet();
        User user = new User();
        UUID creatorId = UUID.randomUUID();
        User creator = new User();
        creator.setId(creatorId);
        toCopy.setUser(creator);
        toCopy.setPrivacy(false);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(toCopy));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(creatorId)).thenReturn(Optional.of(creator));
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.copyCardSet(cardSetId, userId));
        assertEquals("User statistics not found", exception.getMessage());
        verifyNoInteractions(cardSetMapper, cardMapper);
    }

    @Test
    void updateCardSet_updatesCardSetWhenUserIsCreator() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User user = new User();
        CardSetRequest cardSetRequest = new CardSetRequest();
        CardSet updatedCardSet = new CardSet();
        user.setId(userId);
        cardSet.setUser(user);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cardSetMapper.partialUpdateCardSet(cardSetRequest, cardSet)).thenReturn(updatedCardSet);
        cardSetService.updateCardSet(cardSetId, cardSetRequest, userId);
        verify(cardSetRepository).findById(cardSetId);
        verify(userRepository).findById(userId);
        verify(cardSetMapper).partialUpdateCardSet(cardSetRequest, cardSet);
        verify(cardSetRepository).save(updatedCardSet);
    }

    @Test
    void updateCardSet_throwsExceptionWhenCardSetNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSetRequest cardSetRequest = new CardSetRequest();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.updateCardSet(cardSetId, cardSetRequest, userId));
        assertEquals("Card set not found", exception.getMessage());
        verifyNoInteractions(userRepository, cardSetMapper);
    }

    @Test
    void updateCardSet_throwsExceptionWhenUserNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        CardSetRequest cardSetRequest = new CardSetRequest();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.updateCardSet(cardSetId, cardSetRequest, userId));
        assertEquals("User not found", exception.getMessage());
        verifyNoInteractions(cardSetMapper);
    }

    @Test
    void updateCardSet_throwsExceptionWhenUserIsNotCreatorOrAdmin() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User creator = new User();
        User user = new User();
        CardSetRequest cardSetRequest = new CardSetRequest();
        creator.setId(UUID.randomUUID());
        user.setId(userId);
        user.setRole(Role.USER);
        cardSet.setUser(creator);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.updateCardSet(cardSetId, cardSetRequest, userId));
        assertEquals("User is not the creator of the card set", exception.getMessage());
        verifyNoInteractions(cardSetMapper);
    }

    @Test
    void updateFavoriteCardSet_addsCardSetToFavoritesWhenNotAlreadyFavorite() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User user = new User();
        user.setFavoriteSets(new ArrayList<>());
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        cardSetService.updateFavoriteCardSet(cardSetId, true, userId);
        assertTrue(user.getFavoriteSets().contains(cardSet));
        assertTrue(cardSet.getFavoriteUsers().contains(user));
        verify(userRepository).save(user);
    }

    @Test
    void updateFavoriteCardSet_removesCardSetFromFavoritesWhenAlreadyFavorite() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User user = new User();
        user.setFavoriteSets(new ArrayList<>());
        user.getFavoriteSets().add(cardSet);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        cardSetService.updateFavoriteCardSet(cardSetId, false, userId);
        assertFalse(user.getFavoriteSets().contains(cardSet));
        assertFalse(cardSet.getFavoriteUsers().contains(user));
        verify(userRepository).save(user);
    }

    @Test
    void updateFavoriteCardSet_throwsExceptionWhenCardSetNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.updateFavoriteCardSet(cardSetId, true, userId));
        assertEquals("Card set not found", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void updateFavoriteCardSet_throwsExceptionWhenUserNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.updateFavoriteCardSet(cardSetId, true, userId));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getCardSet_throwsExceptionWhenCardSetNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.getCardSet(cardSetId, userId));
        assertEquals("Card set not found", exception.getMessage());
        verifyNoInteractions(userRepository, setStatisticsRepository);
    }

    @Test
    void getCardSet_throwsExceptionWhenUserNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        cardSet.setPrivacy(false);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.getCardSet(cardSetId, userId));
        assertEquals("User not found", exception.getMessage());
        verifyNoInteractions(setStatisticsRepository);
    }

    @Test
    void getCardSet_throwsExceptionWhenCardSetIsPrivateAndUserIsNotCreator() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User creator = new User();
        creator.setId(UUID.randomUUID());
        cardSet.setUser(creator);
        cardSet.setPrivacy(true);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.getCardSet(cardSetId, userId));
        assertEquals("Card set is private", exception.getMessage());
        verifyNoInteractions(setStatisticsRepository);
    }

    @Test
    void getAllCardSets_returnsAllCardSetsWithMappedUserIds() {
        CardSet cardSet1 = new CardSet();
        CardSet cardSet2 = new CardSet();
        User user1 = new User();
        User user2 = new User();
        FullCardSetInfo fullCardSetInfo1 = new FullCardSetInfo();
        FullCardSetInfo fullCardSetInfo2 = new FullCardSetInfo();
        user1.setId(UUID.randomUUID());
        user2.setId(UUID.randomUUID());
        cardSet1.setUser(user1);
        cardSet2.setUser(user2);
        when(cardSetRepository.findAll()).thenReturn(List.of(cardSet1, cardSet2));
        when(cardSetMapper.toFullDto(cardSet1)).thenReturn(fullCardSetInfo1);
        when(cardSetMapper.toFullDto(cardSet2)).thenReturn(fullCardSetInfo2);
        List<FullCardSetInfo> result = cardSetService.getAllCardSets();
        assertEquals(2, result.size());
        assertEquals(user1.getId(), result.get(0).getUserId());
        assertEquals(user2.getId(), result.get(1).getUserId());
        verify(cardSetRepository).findAll();
        verify(cardSetMapper).toFullDto(cardSet1);
        verify(cardSetMapper).toFullDto(cardSet2);
    }

    @Test
    void getAllCardSets_returnsEmptyListWhenNoCardSetsExist() {
        when(cardSetRepository.findAll()).thenReturn(Collections.emptyList());
        List<FullCardSetInfo> result = cardSetService.getAllCardSets();
        assertTrue(result.isEmpty());
        verify(cardSetRepository).findAll();
        verifyNoInteractions(cardSetMapper);
    }

    @Test
    void deleteCardSet_throwsExceptionWhenCardSetNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.deleteCardSet(cardSetId, userId));
        assertEquals("Card set not found", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void deleteCardSet_throwsExceptionWhenUserNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.deleteCardSet(cardSetId, userId));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteCardSet_throwsExceptionWhenUserIsNotCreatorOrAdmin() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User creator = new User();
        User user = new User();
        creator.setId(UUID.randomUUID());
        user.setId(userId);
        user.setRole(Role.USER);
        cardSet.setUser(creator);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.deleteCardSet(cardSetId, userId));
        assertEquals("User is not the creator of the card set", exception.getMessage());
    }

    @Test
    void updateOrderOfCards_reordersCardsSuccessfullyWhenUserIsCreator() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User user = new User();
        Card card1 = new Card();
        Card card2 = new Card();
        CardDto cardDto1 = new CardDto();
        CardDto cardDto2 = new CardDto();
        user.setId(userId);
        cardSet.setUser(user);
        cardSet.setCards(new ArrayList<>());
        cardSet.getCards().add(card1);
        cardSet.getCards().add(card2);
        card1.setId(UUID.randomUUID());
        card2.setId(UUID.randomUUID());
        cardDto1.setId(card2.getId());
        cardDto2.setId(card1.getId());
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        cardSetService.updateOrderOfCards(cardSetId, List.of(cardDto1, cardDto2), userId);
        assertEquals(card2, cardSet.getCards().get(0));
        assertEquals(card1, cardSet.getCards().get(1));
        verify(cardSetRepository).save(cardSet);
    }

    @Test
    void updateOrderOfCards_throwsExceptionWhenCardSetNotFound() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<CardDto> cardDtos = List.of();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.updateOrderOfCards(cardSetId, cardDtos, userId));
        assertEquals("Card set not found", exception.getMessage());
    }

    @Test
    void updateOrderOfCards_throwsExceptionWhenUserIsNotCreator() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User creator = new User();
        creator.setId(UUID.randomUUID());
        cardSet.setUser(creator);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.updateOrderOfCards(cardSetId, List.of(), userId));
        assertEquals("User is not the creator of the card set", exception.getMessage());
    }

    @Test
    void updateOrderOfCards_throwsExceptionWhenCardListSizeDoesNotMatch() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User user = new User();
        user.setId(userId);
        cardSet.setUser(user);
        cardSet.setCards(List.of(new Card()));
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.updateOrderOfCards(cardSetId, List.of(), userId));
        assertEquals("Card list size does not match", exception.getMessage());
    }

    @Test
    void updateOrderOfCards_throwsExceptionWhenCardIdDoesNotMatch() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        User user = new User();
        Card card = new Card();
        CardDto cardDto = new CardDto();
        user.setId(userId);
        cardSet.setUser(user);
        card.setId(UUID.randomUUID());
        cardSet.setCards(List.of(card));
        cardDto.setId(UUID.randomUUID());
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cardSetService.updateOrderOfCards(cardSetId, List.of(cardDto), userId));
        assertEquals("Card ID does not match", exception.getMessage());
    }
}
