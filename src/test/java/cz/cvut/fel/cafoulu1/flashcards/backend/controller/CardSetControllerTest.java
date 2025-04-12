package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardSetRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.CardSetsResponse;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardSetInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardSetMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.CardSetServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CardSetControllerTest {
    @Mock
    private CardSetServiceImpl cardSetService;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private CardSetMapper cardSetMapper;
    @InjectMocks
    private CardSetController cardSetController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCardSet_returnsOkWhenCardSetIsCreatedSuccessfully() {
        CardSetRequest cardSetRequest = new CardSetRequest();
        UUID userId = UUID.randomUUID();
        UUID setId = UUID.randomUUID();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(cardSetService.createCardSet(userId, cardSetRequest)).thenReturn(setId);

        ResponseEntity<?> response = cardSetController.createCardSet(cardSetRequest, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(setId, response.getBody());
        verify(cardSetService).createCardSet(userId, cardSetRequest);
    }

    @Test
    void createCardSet_returnsBadRequestWhenExceptionIsThrown() {
        CardSetRequest cardSetRequest = new CardSetRequest();
        UUID userId = UUID.randomUUID();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(cardSetService.createCardSet(userId, cardSetRequest)).thenThrow(new RuntimeException("Testing error"));

        ResponseEntity<?> response = cardSetController.createCardSet(cardSetRequest, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardSetService).createCardSet(userId, cardSetRequest);
    }

    @Test
    void copyCardSet_returnsOkWhenCardSetIsCopiedSuccessfully() {
        UUID cardSetId = UUID.randomUUID();
        UUID copyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(cardSetService.copyCardSet(cardSetId, userId)).thenReturn(copyId);

        ResponseEntity<?> response = cardSetController.copyCardSet(cardSetId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(copyId, response.getBody());
        verify(cardSetService).copyCardSet(cardSetId, userId);
    }

    @Test
    void copyCardSet_returnsBadRequestWhenExceptionIsThrown() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(cardSetService.copyCardSet(cardSetId, userId)).thenThrow(new RuntimeException("Testing error"));

        ResponseEntity<?> response = cardSetController.copyCardSet(cardSetId, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardSetService).copyCardSet(cardSetId, userId);
    }

    @Test
    void updateCardSet_returnsOkWhenCardSetIsUpdatedSuccessfully() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSetRequest cardSetRequest = new CardSetRequest();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doNothing().when(cardSetService).updateCardSet(cardSetId, cardSetRequest, userId);

        ResponseEntity<?> response = cardSetController.updateCardSet(cardSetId, cardSetRequest, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cardSetService).updateCardSet(cardSetId, cardSetRequest, userId);
    }

    @Test
    void updateCardSet_returnsBadRequestWhenExceptionIsThrown() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSetRequest cardSetRequest = new CardSetRequest();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doThrow(new RuntimeException("Testing error")).when(cardSetService).updateCardSet(cardSetId, cardSetRequest, userId);

        ResponseEntity<?> response = cardSetController.updateCardSet(cardSetId, cardSetRequest, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardSetService).updateCardSet(cardSetId, cardSetRequest, userId);
    }

    @Test
    void updateFavoriteCardSet_returnsOkWhenFavoriteStatusIsUpdatedSuccessfully() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doNothing().when(cardSetService).updateFavoriteCardSet(cardSetId, true, userId);

        ResponseEntity<?> response = cardSetController.updateFavoriteCardSet(cardSetId, true, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cardSetService).updateFavoriteCardSet(cardSetId, true, userId);
    }

    @Test
    void updateFavoriteCardSet_returnsBadRequestWhenExceptionIsThrown() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doThrow(new RuntimeException("Testing error")).when(cardSetService).updateFavoriteCardSet(cardSetId, true, userId);

        ResponseEntity<?> response = cardSetController.updateFavoriteCardSet(cardSetId, true, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardSetService).updateFavoriteCardSet(cardSetId, true, userId);
    }

    @Test
    void deleteCardSet_returnsOkWhenCardSetIsDeletedSuccessfully() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doNothing().when(cardSetService).deleteCardSet(cardSetId, userId);

        ResponseEntity<?> response = cardSetController.deleteCardSet(cardSetId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cardSetService).deleteCardSet(cardSetId, userId);
    }

    @Test
    void deleteCardSet_returnsBadRequestWhenExceptionIsThrown() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doThrow(new RuntimeException("Testing error")).when(cardSetService).deleteCardSet(cardSetId, userId);

        ResponseEntity<?> response = cardSetController.deleteCardSet(cardSetId, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardSetService).deleteCardSet(cardSetId, userId);
    }

    @Test
    void getCardSet_returnsOkWhenCardSetIsRetrievedSuccessfully() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardSetDto cardSet = new CardSetDto();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(cardSetService.getCardSet(cardSetId, userId)).thenReturn(cardSet);

        ResponseEntity<?> response = cardSetController.getCardSet(cardSetId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cardSet, response.getBody());
        verify(cardSetService).getCardSet(cardSetId, userId);
    }

    @Test
    void getCardSet_returnsBadRequestWhenExceptionIsThrown() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(cardSetService.getCardSet(cardSetId, userId)).thenThrow(new RuntimeException("Testing error"));

        ResponseEntity<?> response = cardSetController.getCardSet(cardSetId, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardSetService).getCardSet(cardSetId, userId);
    }

    @Test
    void getAllCardSets_returnsOkWhenCardSetsAreRetrievedSuccessfully() {
        List<FullCardSetInfo> cardSets = List.of(new FullCardSetInfo(), new FullCardSetInfo());

        when(cardSetService.getAllCardSets()).thenReturn(cardSets);

        ResponseEntity<?> response = cardSetController.getAllCardSets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cardSets, response.getBody());
        verify(cardSetService).getAllCardSets();
    }

    @Test
    void getAllCardSets_returnsBadRequestWhenExceptionIsThrown() {
        when(cardSetService.getAllCardSets()).thenThrow(new RuntimeException("Testing error"));

        ResponseEntity<?> response = cardSetController.getAllCardSets();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardSetService).getAllCardSets();
    }

    @Test
    void getAllCards_returnsOkWhenCardsAreRetrievedSuccessfully() {
        List<FullCardInfo> cards = List.of(new FullCardInfo(), new FullCardInfo());

        when(cardSetService.getAllCards()).thenReturn(cards);

        ResponseEntity<?> response = cardSetController.getAllCards();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cards, response.getBody());
        verify(cardSetService).getAllCards();
    }

    @Test
    void getAllCards_returnsBadRequestWhenExceptionIsThrown() {
        when(cardSetService.getAllCards()).thenThrow(new RuntimeException("Testing error"));

        ResponseEntity<?> response = cardSetController.getAllCards();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardSetService).getAllCards();
    }

    @Test
    void updateCardOrder_returnsOkWhenCardOrderIsUpdatedSuccessfully() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<CardDto> cards = List.of(new CardDto(), new CardDto());
        Map<String, List<CardDto>> orderList = Map.of("cards", cards);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doNothing().when(cardSetService).updateOrderOfCards(cardSetId, cards, userId);

        ResponseEntity<?> response = cardSetController.updateCardOrder(cardSetId, orderList, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cardSetService).updateOrderOfCards(cardSetId, cards, userId);
    }

    @Test
    void updateCardOrder_returnsBadRequestWhenCardsListIsMissing() {
        UUID cardSetId = UUID.randomUUID();
        Map<String, List<CardDto>> orderList = Map.of("cards", List.of());

        ResponseEntity<?> response = cardSetController.updateCardOrder(cardSetId, orderList, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cards list is required", response.getBody());
        verifyNoInteractions(cardSetService);
    }

    @Test
    void updateCardOrder_returnsBadRequestWhenExceptionIsThrown() {
        UUID cardSetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<CardDto> cards = List.of(new CardDto(), new CardDto());
        Map<String, List<CardDto>> orderList = Map.of("cards", cards);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doThrow(new RuntimeException("Testing error")).when(cardSetService).updateOrderOfCards(cardSetId, cards, userId);

        ResponseEntity<?> response = cardSetController.updateCardOrder(cardSetId, orderList, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardSetService).updateOrderOfCards(cardSetId, cards, userId);
    }

    @Test
    void getAllCardSetsWithPagination_returnsOkWhenCardSetsAreRetrievedSuccessfully() {
        UUID userId = UUID.randomUUID();
        FilterCardSetsRequest filterRequest = new FilterCardSetsRequest();
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "creationDate"));
        CardSetsResponse cardSets = new CardSetsResponse();
        cardSets.setCardSets(List.of());
        cardSets.setPages(1);
        cardSets.setCategories(List.of());

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(cardSetMapper.createFilterCardSetsRequest("", "", false, false, userId)).thenReturn(filterRequest);
        when(cardSetService.getFilteredCardSets(any(Pageable.class), eq(filterRequest))).thenReturn(cardSets);

        ResponseEntity<?> response = cardSetController.getAllCardSetsWithPagination(1, 20, "creationDate", "desc", "", "", false, false, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cardSets, response.getBody());
        verify(cardSetMapper).createFilterCardSetsRequest("", "", false, false, userId);
        verify(cardSetService).getFilteredCardSets(pageable, filterRequest);
    }

    @Test
    void getAllCardSetsWithPagination_returnsBadRequestWhenExceptionIsThrown() {
        UUID userId = UUID.randomUUID();
        FilterCardSetsRequest filterRequest = new FilterCardSetsRequest();
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "creationDate"));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(cardSetMapper.createFilterCardSetsRequest("", "", false, false, userId)).thenReturn(filterRequest);
        when(cardSetService.getFilteredCardSets(any(Pageable.class), eq(filterRequest))).thenThrow(new RuntimeException("Testing error"));

        ResponseEntity<?> response = cardSetController.getAllCardSetsWithPagination(1, 20, "creationDate", "desc", "", "", false, false, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardSetMapper).createFilterCardSetsRequest("", "", false, false, userId);
        verify(cardSetService).getFilteredCardSets(pageable, filterRequest);
    }
}
