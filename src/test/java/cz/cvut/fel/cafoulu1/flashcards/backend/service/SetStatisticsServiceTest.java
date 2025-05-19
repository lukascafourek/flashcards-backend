package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.SetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.SetStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.CardSetRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.SetStatisticsRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SetStatisticsServiceTest {
    @Mock
    private SetStatisticsRepository setStatisticsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardSetRepository cardSetRepository;
    @Mock
    private SetStatisticsMapper setStatisticsMapper;
    @InjectMocks
    private SetStatisticsServiceImpl setStatisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSetStatistics_shouldReturnStatisticsWhenCardSetAndUserExist() {
        UUID userId = UUID.randomUUID();
        UUID cardSetId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        cardSet.setId(cardSetId);
        cardSet.setPrivacy(false);
        User user = new User();
        user.setId(userId);
        SetStatistics setStatistics = new SetStatistics();
        setStatistics.setCardSet(cardSet);
        setStatistics.setUser(user);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(setStatisticsRepository.findByCardSetIdAndUserId(cardSetId, userId)).thenReturn(Optional.of(setStatistics));
        when(setStatisticsMapper.toDto(setStatistics)).thenReturn(new SetStatisticsDto());
        SetStatisticsDto result = setStatisticsService.getSetStatistics(userId, cardSetId);
        assertNotNull(result);
        verify(cardSetRepository).findById(cardSetId);
        verify(userRepository).findById(userId);
        verify(setStatisticsRepository).findByCardSetIdAndUserId(cardSetId, userId);
    }

    @Test
    void getSetStatistics_shouldThrowWhenCardSetNotFound() {
        UUID userId = UUID.randomUUID();
        UUID cardSetId = UUID.randomUUID();
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> setStatisticsService.getSetStatistics(userId, cardSetId));
        verify(cardSetRepository).findById(cardSetId);
        verifyNoInteractions(userRepository, setStatisticsRepository, setStatisticsMapper);
    }

    @Test
    void getSetStatistics_shouldThrowWhenCardSetIsPrivateAndUserNotOwner() {
        UUID userId = UUID.randomUUID();
        UUID cardSetId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        cardSet.setId(cardSetId);
        cardSet.setPrivacy(true);
        User owner = new User();
        owner.setId(UUID.randomUUID());
        cardSet.setUser(owner);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        assertThrows(IllegalArgumentException.class, () -> setStatisticsService.getSetStatistics(userId, cardSetId));
        verify(cardSetRepository).findById(cardSetId);
        verifyNoInteractions(userRepository, setStatisticsRepository, setStatisticsMapper);
    }

    @Test
    void getSetStatistics_shouldThrowWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        UUID cardSetId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        cardSet.setId(cardSetId);
        cardSet.setPrivacy(false);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> setStatisticsService.getSetStatistics(userId, cardSetId));
        verify(cardSetRepository).findById(cardSetId);
        verify(userRepository).findById(userId);
        verifyNoInteractions(setStatisticsRepository, setStatisticsMapper);
    }

    @Test
    void getSetStatistics_shouldCreateNewStatisticsWhenNotFound() {
        UUID userId = UUID.randomUUID();
        UUID cardSetId = UUID.randomUUID();
        CardSet cardSet = new CardSet();
        cardSet.setId(cardSetId);
        cardSet.setPrivacy(false);
        User user = new User();
        user.setId(userId);
        SetStatistics newSetStatistics = new SetStatistics();
        newSetStatistics.setCardSet(cardSet);
        newSetStatistics.setUser(user);
        when(cardSetRepository.findById(cardSetId)).thenReturn(Optional.of(cardSet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(setStatisticsRepository.findByCardSetIdAndUserId(cardSetId, userId)).thenReturn(Optional.empty());
        when(setStatisticsRepository.save(any(SetStatistics.class))).thenReturn(newSetStatistics);
        when(setStatisticsMapper.toDto(newSetStatistics)).thenReturn(new SetStatisticsDto());
        SetStatisticsDto result = setStatisticsService.getSetStatistics(userId, cardSetId);
        assertNotNull(result);
        verify(setStatisticsRepository).save(any(SetStatistics.class));
        verify(cardSetRepository).save(cardSet);
        verify(userRepository).save(user);
    }
}
