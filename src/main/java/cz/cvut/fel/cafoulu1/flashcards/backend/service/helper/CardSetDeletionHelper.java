package cz.cvut.fel.cafoulu1.flashcards.backend.service.helper;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.CardRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.PictureRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.SetStatisticsRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Helper class for deletion of card sets. It deletes all related entities.
 * Singleton class.
 */
public class CardSetDeletionHelper {
    private static CardSetDeletionHelper instance;

    private CardSetDeletionHelper() {
    }

    public static CardSetDeletionHelper getInstance() {
        if (instance == null) {
            instance = new CardSetDeletionHelper();
        }
        return instance;
    }

    @Transactional
    public void cardSetDeleteHelper(CardSet cardSet, UserRepository userRepository, SetStatisticsRepository setStatisticsRepository, PictureRepository pictureRepository, CardRepository cardRepository) {
        cardSet.getFavoriteUsers().forEach(user1 -> user1.getFavoriteSets().remove(cardSet));
        userRepository.saveAll(cardSet.getFavoriteUsers());
        List<User> setStatisticsUsers = cardSet.getSetStatistics().stream()
                .map(setStatistics -> {
                    User user2 = setStatistics.getUser();
                    user2.getSetStatistics().remove(setStatistics);
                    return user2;
                })
                .toList();
        userRepository.saveAll(setStatisticsUsers);
        setStatisticsRepository.deleteAll(cardSet.getSetStatistics());
        cardSet.getCards().forEach((card) ->
                pictureRepository.findById(card.getId()).ifPresent(picture -> {
                    User user = cardSet.getUser();
                    pictureRepository.delete(picture);
                    user.setNumberOfImages(user.getNumberOfImages() - 1);
                    userRepository.save(user);
                }));
        cardRepository.deleteAll(cardSet.getCards());
    }
}
