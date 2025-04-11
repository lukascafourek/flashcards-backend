package cz.cvut.fel.cafoulu1.flashcards.backend.service.helper;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.CardRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.PictureRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.SetStatisticsRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class CardSetDeletionHelperTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private SetStatisticsRepository setStatisticsRepository;
    @Mock
    private PictureRepository pictureRepository;
    @Mock
    private CardRepository cardRepository;
    @InjectMocks
    private CardSetDeletionHelper cardSetDeletionHelper = CardSetDeletionHelper.getInstance();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cardSetDeleteHelper_removesFavoriteSetsFromUsers() {
        CardSet cardSet = new CardSet();
        User user1 = new User();
        user1.setFavoriteSets(new ArrayList<>());
        user1.getFavoriteSets().add(cardSet);
        cardSet.setFavoriteUsers(new ArrayList<>());
        cardSet.getFavoriteUsers().add(user1);

        when(userRepository.saveAll(cardSet.getFavoriteUsers())).thenReturn(List.of(user1));

        cardSetDeletionHelper.cardSetDeleteHelper(cardSet, userRepository, setStatisticsRepository, pictureRepository, cardRepository);

        assertTrue(user1.getFavoriteSets().isEmpty());
        verify(userRepository).saveAll(cardSet.getFavoriteUsers());
    }

    @Test
    void cardSetDeleteHelper_removesSetStatisticsFromUsers() {
        CardSet cardSet = new CardSet();
        User user = new User();
        SetStatistics setStatistics = new SetStatistics();
        setStatistics.setUser(user);
        cardSet.setSetStatistics(new ArrayList<>());
        cardSet.getSetStatistics().add(setStatistics);
        user.setSetStatistics(new ArrayList<>());
        user.getSetStatistics().add(setStatistics);

        when(userRepository.saveAll(anyList())).thenReturn(List.of(user));

        cardSetDeletionHelper.cardSetDeleteHelper(cardSet, userRepository, setStatisticsRepository, pictureRepository, cardRepository);

        assertTrue(user.getSetStatistics().isEmpty());
        verify(setStatisticsRepository).deleteAll(cardSet.getSetStatistics());
        verify(userRepository, times(2)).saveAll(anyList());
    }

    @Test
    void cardSetDeleteHelper_deletesPicturesAndUpdatesUserImageCount() {
        CardSet cardSet = new CardSet();
        User user = new User();
        user.setNumberOfImages(1);
        cardSet.setUser(user);
        Card card = new Card();
        card.setId(UUID.randomUUID());
        cardSet.setCards(List.of(card));
        Picture picture = new Picture();
        picture.setCard(card);

        when(pictureRepository.findById(card.getId())).thenReturn(Optional.of(picture));

        cardSetDeletionHelper.cardSetDeleteHelper(cardSet, userRepository, setStatisticsRepository, pictureRepository, cardRepository);

        assertEquals(0, user.getNumberOfImages());
        verify(pictureRepository).delete(picture);
        verify(userRepository).save(user);
    }

    @Test
    void cardSetDeleteHelper_deletesAllCardsFromCardSet() {
        CardSet cardSet = new CardSet();
        Card card1 = new Card();
        Card card2 = new Card();
        cardSet.setCards(List.of(card1, card2));

        cardSetDeletionHelper.cardSetDeleteHelper(cardSet, userRepository, setStatisticsRepository, pictureRepository, cardRepository);

        verify(cardRepository).deleteAll(cardSet.getCards());
    }
}
