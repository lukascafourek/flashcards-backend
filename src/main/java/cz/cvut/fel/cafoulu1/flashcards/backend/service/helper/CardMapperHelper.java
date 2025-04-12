package cz.cvut.fel.cafoulu1.flashcards.backend.service.helper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Card;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.PictureRepository;
import org.apache.tika.Tika;

import java.util.Base64;

/**
 * Helper class for mapping {@link Card} to {@link CardDto}.
 * Singleton class.
 */
public class CardMapperHelper {
    private final Tika tika = new Tika();

    private static CardMapperHelper instance;

    private CardMapperHelper() {
    }

    public static CardMapperHelper getInstance() {
        if (instance == null) {
            instance = new CardMapperHelper();
        }
        return instance;
    }

    public CardDto mapCardToDto(Card card, CardMapper cardMapper, PictureRepository pictureRepository) {
        CardDto cardDto = cardMapper.toDto(card);
        pictureRepository.findById(card.getId()).ifPresent(picture -> {
            String encodedPicture = Base64.getEncoder().encodeToString(picture.getPicture());
            cardDto.setPicture(encodedPicture);
            cardDto.setMimeType(tika.detect(picture.getPicture()));
        });
        return cardDto;
    }

    public FullCardInfo mapCardToFullInfo(Card card, CardMapper cardMapper, PictureRepository pictureRepository) {
        FullCardInfo fullCardInfo = cardMapper.toFullDto(card);
        fullCardInfo.setCardSetId(card.getCardSet().getId());
        pictureRepository.findById(card.getId()).ifPresent(picture -> {
            String encodedPicture = Base64.getEncoder().encodeToString(picture.getPicture());
            fullCardInfo.setPicture(encodedPicture);
            fullCardInfo.setMimeType(tika.detect(picture.getPicture()));
        });
        return fullCardInfo;
    }
}
