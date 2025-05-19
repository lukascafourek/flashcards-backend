package cz.cvut.fel.cafoulu1.flashcards.backend.service.helper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Card;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Picture;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.PictureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class CardMapperHelperTest {
    @Mock
    private CardMapper cardMapper;
    @Mock
    private PictureRepository pictureRepository;
    @InjectMocks
    private CardMapperHelper cardMapperHelper = CardMapperHelper.getInstance();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mapCardToDto_returnsCardDtoWithPictureAndMimeTypeWhenPictureExists() {
        Card card = new Card();
        card.setId(UUID.randomUUID());
        CardDto cardDto = new CardDto();
        Picture picture = new Picture();
        picture.setPicture(new byte[]{1, 2, 3});
        when(cardMapper.toDto(card)).thenReturn(cardDto);
        when(pictureRepository.findById(card.getId())).thenReturn(Optional.of(picture));
        CardDto result = cardMapperHelper.mapCardToDto(card, cardMapper, pictureRepository);
        assertEquals(Base64.getEncoder().encodeToString(picture.getPicture()), result.getPicture());
        assertEquals("application/octet-stream", result.getMimeType());
    }

    @Test
    void mapCardToDto_returnsCardDtoWithoutPictureAndMimeTypeWhenPictureDoesNotExist() {
        Card card = new Card();
        card.setId(UUID.randomUUID());
        CardDto cardDto = new CardDto();
        when(cardMapper.toDto(card)).thenReturn(cardDto);
        when(pictureRepository.findById(card.getId())).thenReturn(Optional.empty());
        CardDto result = cardMapperHelper.mapCardToDto(card, cardMapper, pictureRepository);
        assertNull(result.getPicture());
        assertNull(result.getMimeType());
    }

    @Test
    void mapCardToFullInfo_returnsFullCardInfoWithPictureAndMimeTypeWhenPictureExists() {
        Card card = new Card();
        card.setId(UUID.randomUUID());
        CardSet cardSet = new CardSet();
        cardSet.setId(UUID.randomUUID());
        card.setCardSet(cardSet);
        FullCardInfo fullCardInfo = new FullCardInfo();
        Picture picture = new Picture();
        picture.setPicture(new byte[]{1, 2, 3});
        when(cardMapper.toFullDto(card)).thenReturn(fullCardInfo);
        when(pictureRepository.findById(card.getId())).thenReturn(Optional.of(picture));
        FullCardInfo result = cardMapperHelper.mapCardToFullInfo(card, cardMapper, pictureRepository);
        assertEquals(cardSet.getId(), result.getCardSetId());
        assertEquals(Base64.getEncoder().encodeToString(picture.getPicture()), result.getPicture());
        assertEquals("application/octet-stream", result.getMimeType());
    }

    @Test
    void mapCardToFullInfo_returnsFullCardInfoWithoutPictureAndMimeTypeWhenPictureDoesNotExist() {
        Card card = new Card();
        card.setId(UUID.randomUUID());
        CardSet cardSet = new CardSet();
        cardSet.setId(UUID.randomUUID());
        card.setCardSet(cardSet);
        FullCardInfo fullCardInfo = new FullCardInfo();
        when(cardMapper.toFullDto(card)).thenReturn(fullCardInfo);
        when(pictureRepository.findById(card.getId())).thenReturn(Optional.empty());
        FullCardInfo result = cardMapperHelper.mapCardToFullInfo(card, cardMapper, pictureRepository);
        assertEquals(cardSet.getId(), result.getCardSetId());
        assertNull(result.getPicture());
        assertNull(result.getMimeType());
    }
}
