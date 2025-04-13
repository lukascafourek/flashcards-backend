package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardSetRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardSetInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.CardSetsResponse;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.CardSetMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.SetStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.cor.*;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.helper.CardSetDeletionHelper;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.helper.CardMapperHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        return saveCardSetRelations(user, cardSet, userStatistics);
    }

    @Transactional
    @Override
    public UUID copyCardSet(UUID cardSetId, UUID userId) {
        CardSet toCopy = cardSetRepository.findById(cardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Card set not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (toCopy.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is the creator of the card set");
        }
        if (toCopy.getPrivacy()) {
            throw new IllegalArgumentException("Card set is private");
        }
        UserStatistics userStatistics = userStatisticsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User statistics not found"));
        CardSetRequest cardSetRequest = cardSetMapper.createCardSetRequest(toCopy);
        CardSet copiedCardSet = cardSetMapper.createCardSet(cardSetRequest);
        copiedCardSet.setPrivacy(true);
        List<Card> copiedCards = toCopy.getCards().stream()
                .map(card -> {
                    CardRequest cardRequest = cardMapper.createCardRequest(card);
                    Card copiedCard = cardMapper.createCard(cardRequest);
                    copiedCard.setCardSet(copiedCardSet);
                    copiedCard.setCardOrder(card.getCardOrder());
                    return copiedCard;
                })
                .toList();
        copiedCardSet.setCards(copiedCards);
        userStatistics.setCardsCreated(userStatistics.getCardsCreated() + copiedCards.size());
        UUID copiedCardSetId = saveCardSetRelations(user, copiedCardSet, userStatistics);
        cardRepository.saveAll(copiedCardSet.getCards());
        return copiedCardSetId;
    }

    @Transactional
    protected UUID saveCardSetRelations(User user, CardSet cardSet, UserStatistics userStatistics) {
        cardSet.setCreationDate(LocalDate.now());
        cardSet.setUser(user);
        SetStatistics setStatistics = new SetStatistics();
        setStatistics.setCardSet(cardSet);
        setStatistics.setUser(user);
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!cardSet.getUser().getId().equals(userId) && !user.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("User is not the creator of the card set");
        }
        CardSet updatedCardSet = cardSetMapper.partialUpdateCardSet(cardSetRequest, cardSet);
        cardSetRepository.save(updatedCardSet);
    }

    @Transactional
    @Override
    public void updateFavoriteCardSet(UUID cardSetId, Boolean isFavorite, UUID userId) {
        CardSet cardSet = cardSetRepository.findById(cardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Card set not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (isFavorite != null && isFavorite && !user.getFavoriteSets().contains(cardSet)) {
            user.getFavoriteSets().add(cardSet);
            cardSet.getFavoriteUsers().add(user);
        } else if (isFavorite != null && !isFavorite && user.getFavoriteSets().contains(cardSet)) {
            user.getFavoriteSets().remove(cardSet);
            cardSet.getFavoriteUsers().remove(user);
        }
        userRepository.save(user);
    }

    @Transactional
    @Override
    public CardSetDto getCardSet(UUID cardSetId, UUID userId) {
        CardSet cardSet = cardSetRepository.findById(cardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Card set not found"));
        if (cardSet.getPrivacy() && !cardSet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Card set is private");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        SetStatistics setStatistics = setStatisticsRepository.findByCardSetIdAndUserId(cardSetId, userId)
                .orElseGet(() -> {
                    SetStatistics newSetStatistics = new SetStatistics();
                    newSetStatistics.setCardSet(cardSet);
                    newSetStatistics.setUser(user);
                    return setStatisticsRepository.save(newSetStatistics);
                });
        cardSet.getSetStatistics().add(setStatistics);
        cardSetRepository.save(cardSet);
        user.getSetStatistics().add(setStatistics);
        userRepository.save(user);
        List<CardDto> cards = cardSet.getCards().stream()
                .map(card -> CardMapperHelper.getInstance().mapCardToDto(card, cardMapper, pictureRepository))
                .toList();
        CardSetDto dto = new CardSetDto();
        BasicCardSetDto basicCardSetDto = cardSetMapper.toDtoBasic(cardSet);
        basicCardSetDto.setCreator(cardSet.getUser().getUsername());
        dto.setBasicCardSetDto(basicCardSetDto);
        dto.setSetStatistics(setStatisticsMapper.toDtoBasic(setStatistics));
        dto.setCards(List.copyOf(cards));
        dto.setFavorite(cardSet.getFavoriteUsers().contains(user));
        dto.setCreator(cardSet.getUser().getId().equals(userId));
        dto.setPrivacy(cardSet.getPrivacy());
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
                new CreatorFilter(),
                new PublicOrOwnedFilter()
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
    public List<FullCardSetInfo> getAllCardSets() {
        return cardSetRepository.findAll().stream()
                .map(cardSet -> {
                    FullCardSetInfo fullCardSetInfo = cardSetMapper.toFullDto(cardSet);
                    fullCardSetInfo.setUserId(cardSet.getUser().getId());
                    return fullCardSetInfo;
                })
                .toList();
    }

    @Transactional
    @Override
    public List<FullCardInfo> getAllCards() {
        return cardRepository.findAll().stream()
                .map(card -> CardMapperHelper.getInstance().mapCardToFullInfo(card, cardMapper, pictureRepository))
                .toList();
    }

    @Transactional
    @Override
    public void deleteCardSet(UUID cardSetId, UUID userId) {
        CardSet cardSet = cardSetRepository.findById(cardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Card set not found"));
        User creator = cardSet.getUser();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!creator.getId().equals(userId) && !user.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("User is not the creator of the card set");
        }
        CardSetDeletionHelper.getInstance()
                .cardSetDeleteHelper(cardSet, userRepository, setStatisticsRepository, pictureRepository, cardRepository);
        creator.getCardSets().remove(cardSet);
        userRepository.save(creator);
        cardSetRepository.delete(cardSet);
    }

    @Transactional
    @Override
    public void updateOrderOfCards(UUID cardSetId, List<CardDto> cardDtos, UUID userId) {
        CardSet cardSet = cardSetRepository.findById(cardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Card set not found"));
        if (!cardSet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not the creator of the card set");
        }
        if (cardDtos.size() != cardSet.getCards().size()) {
            throw new IllegalArgumentException("Card list size does not match");
        }
        List<Card> reorderedCards = cardDtos.stream()
                .map(cardDto -> cardSet.getCards().stream()
                        .filter(card -> card.getId().equals(cardDto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Card ID does not match")))
                .toList();
        cardSet.getCards().clear();
        for (Card card : reorderedCards) {
            card.setCardSet(cardSet);
            cardSet.getCards().add(card);
        }
        cardSetRepository.save(cardSet);
    }
}
