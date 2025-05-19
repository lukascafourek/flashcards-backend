package cz.cvut.fel.cafoulu1.flashcards.backend.service;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.SetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.mapper.SetStatisticsMapper;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.CardSetRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.SetStatisticsRepository;
import cz.cvut.fel.cafoulu1.flashcards.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of {@link SetStatisticsService} for set statistics.
 */
@Service
@RequiredArgsConstructor
public class SetStatisticsServiceImpl implements SetStatisticsService {
    private final SetStatisticsRepository setStatisticsRepository;

    private final CardSetRepository cardSetRepository;

    private final UserRepository userRepository;

    private final SetStatisticsMapper setStatisticsMapper;

    @Override
    @Transactional
    public SetStatisticsDto getSetStatistics(UUID userId, UUID cardSetId) {
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
        return setStatisticsMapper.toDto(setStatistics);
    }
}
