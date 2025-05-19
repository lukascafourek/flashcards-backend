package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.CardServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CardControllerTest {
    @Mock
    private CardServiceImpl cardService;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;
    @InjectMocks
    private CardController cardController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCard_returnsOkWhenCardIsCreatedSuccessfully() {
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        CardRequest cardRequest = new CardRequest();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(cardService.createCard(setId, cardRequest, userId)).thenReturn(cardId);
        ResponseEntity<?> response = cardController.createCard(setId, cardRequest, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cardId, response.getBody());
        verify(cardService).createCard(setId, cardRequest, userId);
    }

    @Test
    void createCard_returnsBadRequestWhenExceptionIsThrown() {
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CardRequest cardRequest = new CardRequest();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        when(cardService.createCard(setId, cardRequest, userId)).thenThrow(new RuntimeException("Testing error"));
        ResponseEntity<?> response = cardController.createCard(setId, cardRequest, authentication);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardService).createCard(setId, cardRequest, userId);
    }

    @Test
    void updateCard_returnsOkWhenCardIsUpdatedSuccessfully() {
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        CardRequest cardRequest = new CardRequest();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doNothing().when(cardService).updateCard(setId, cardId, cardRequest, userId);
        ResponseEntity<?> response = cardController.updateCard(setId, cardId, cardRequest, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cardService).updateCard(setId, cardId, cardRequest, userId);
    }

    @Test
    void updateCard_returnsBadRequestWhenExceptionIsThrown() {
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        CardRequest cardRequest = new CardRequest();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doThrow(new RuntimeException("Testing error")).when(cardService).updateCard(setId, cardId, cardRequest, userId);
        ResponseEntity<?> response = cardController.updateCard(setId, cardId, cardRequest, authentication);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardService).updateCard(setId, cardId, cardRequest, userId);
    }

    @Test
    void getCards_returnsOkWithListOfCards() {
        UUID setId = UUID.randomUUID();
        List<CardDto> cards = List.of(new CardDto(), new CardDto());
        when(cardService.getCards(setId)).thenReturn(cards);
        ResponseEntity<?> response = cardController.getCards(setId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cards, response.getBody());
        verify(cardService).getCards(setId);
    }

    @Test
    void getCards_returnsBadRequestWhenExceptionIsThrown() {
        UUID setId = UUID.randomUUID();
        when(cardService.getCards(setId)).thenThrow(new RuntimeException("Testing error"));
        ResponseEntity<?> response = cardController.getCards(setId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardService).getCards(setId);
    }

    @Test
    void deleteCard_returnsOkWhenCardIsDeletedSuccessfully() {
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doNothing().when(cardService).deleteCard(setId, cardId, userId);
        ResponseEntity<?> response = cardController.deleteCard(setId, cardId, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cardService).deleteCard(setId, cardId, userId);
    }

    @Test
    void deleteCard_returnsBadRequestWhenExceptionIsThrown() {
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(userId);
        doThrow(new RuntimeException("Testing error")).when(cardService).deleteCard(setId, cardId, userId);
        ResponseEntity<?> response = cardController.deleteCard(setId, cardId, authentication);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Testing error", response.getBody());
        verify(cardService).deleteCard(setId, cardId, userId);
    }
}
