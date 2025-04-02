package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardSetRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.CardSetsResponse;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardSetMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.SetStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.cor.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.helper.CardSetDeletionHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link CardSetService}.
 */
@Service
@RequiredArgsConstructor
public class CardSetServiceImpl implements CardSetService {
    private final CardSetRepository cardSetRepository;

    private final UserRepository userRepository;

    private final CardRepository cardRepository;

    private final PictureRepository pictureRepository;

    private final SetStatisticsRepository setStatisticsRepository;

    private final UserStatisticsRepository userStatisticsRepository;

    private final CardSetMapper cardSetMapper;

    private final CardMapper cardMapper;

    private final SetStatisticsMapper setStatisticsMapper;

    @Transactional
    @Override
    public UUID createCardSet(UUID userId, CardSetRequest cardSetRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        CardSet cardSet = cardSetMapper.createCardSet(cardSetRequest);
        cardSet.setCreationDate(LocalDate.now());
        cardSet.setUser(user);
        SetStatistics setStatistics = new SetStatistics();
        setStatistics.setCardSet(cardSet);
        setStatistics.setUser(user);
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        userStatistics.setSetsCreated(userStatistics.getSetsCreated() + 1);
        userStatisticsRepository.save(userStatistics);
        cardSet.getSetStatistics().add(setStatistics);
        cardSetRepository.save(cardSet);
        setStatisticsRepository.save(setStatistics);
        user.getCardSets().add(cardSet);
        user.getSetStatistics().add(setStatistics);
        userRepository.save(user);
        return cardSet.getId();
    }

    @Transactional
    @Override
    public void updateCardSet(UUID cardSetId, CardSetRequest cardSetRequest, UUID userId) {
        CardSet cardSet = cardSetRepository.findById(cardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Card set not found"));
        if (!cardSet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not the creator of the card set");
        }
        CardSet updatedCardSet = cardSetMapper.partialUpdateCardSet(cardSetRequest, cardSet);
        Boolean isFavorite = cardSetRequest.getFavorite();
        if (isFavorite != null) {
            User user = updatedCardSet.getUser();
            if (isFavorite && !user.getFavoriteSets().contains(updatedCardSet)) {
                user.getFavoriteSets().add(updatedCardSet);
                updatedCardSet.getFavoriteUsers().add(user);
            } else if (!isFavorite && user.getFavoriteSets().contains(updatedCardSet)) {
                user.getFavoriteSets().remove(updatedCardSet);
                updatedCardSet.getFavoriteUsers().remove(user);
            }
            userRepository.save(user);
        }
        cardSetRepository.save(updatedCardSet);
    }

    @Transactional
    @Override
    public CardSetDto getCardSet(UUID cardSetId, UUID userId) {
        CardSet cardSet = cardSetRepository.findById(cardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Card set not found"));
        SetStatistics setStatistics = setStatisticsRepository.findByCardSetIdAndUserId(cardSetId, cardSet.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Set statistics not found"));
        List<CardDto> cards = cardSet.getCards().stream().map(card -> {
            CardDto cardDto = cardMapper.toDto(card);
            pictureRepository.findById(card.getId()).ifPresent(picture -> {
                String encodedPicture = Base64.getEncoder().encodeToString(picture.getPicture());
                cardDto.setPicture(encodedPicture);
            });
            return cardDto;
        }).toList();
        CardSetDto dto = new CardSetDto();
        BasicCardSetDto basicCardSetDto = cardSetMapper.toDtoBasic(cardSet);
        basicCardSetDto.setCreator(cardSet.getUser().getUsername());
        dto.setBasicCardSetDto(basicCardSetDto);
        dto.setSetStatistics(setStatisticsMapper.toDtoBasic(setStatistics));
        dto.setCards(List.copyOf(cards));
        dto.setFavorite(cardSet.getFavoriteUsers().contains(cardSet.getUser()));
        dto.setCreator(cardSet.getUser().getId().equals(userId));
        dto.setCategories(List.of(Category.values()));
        dto.setDescription(cardSet.getDescription());
        return dto;
    }

    @Transactional
    @Override
    public CardSetsResponse getFilteredCardSets(Pageable pageable, FilterCardSetsRequest filterCardSetsRequest) {
        Specification<CardSet> spec = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        List<CardSetFilter> filters = List.of(
                new SearchFilter(),
                new CategoryFilter(),
                new FavoriteFilter(),
                new CreatorFilter()
        );
        for (CardSetFilter filter : filters) {
            spec = filter.apply(filterCardSetsRequest, spec);
        }
        List<BasicCardSetDto> cardSets = cardSetRepository.findAll(spec, pageable).stream()
                .map(cardSet -> {
                    BasicCardSetDto basicCardSetDto = cardSetMapper.toDtoBasic(cardSet);
                    basicCardSetDto.setCreator(cardSet.getUser().getUsername());
                    return basicCardSetDto;
                })
                .toList();
        CardSetsResponse response = new CardSetsResponse();
        int size = cardSetRepository.findAll().size();
        if (size > 0) {
            response.setPages((int) Math.ceil((double) size / pageable.getPageSize()));
        } else {
            response.setPages(1);
        }
        response.setCategories(List.of(Category.values()));
        response.setCardSets(List.copyOf(cardSets));
        return response;
    }

    @Transactional
    @Override
    public void deleteCardSet(UUID cardSetId, UUID userId) {
        CardSet cardSet = cardSetRepository.findById(cardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Card set not found"));
        User creator = cardSet.getUser();
        if (!creator.getId().equals(userId)) {
            throw new IllegalArgumentException("User is not the creator of the card set");
        }
        CardSetDeletionHelper.getInstance()
                .cardSetDeleteHelper(cardSet, userRepository, setStatisticsRepository, pictureRepository, cardRepository);
        creator.getCardSets().remove(cardSet);
        userRepository.save(creator);
        cardSetRepository.delete(cardSet);
    }
}
