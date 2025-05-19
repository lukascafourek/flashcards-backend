package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserStatisticsMapper {
    UserStatisticsDto toDto(UserStatistics userStatistics);

    @Named("incrementStatistic")
    default void incrementStatistic(UserStatistics userStatistics, String statistic) {
        switch (statistic.toLowerCase()) {
            case "setslearned" -> userStatistics.setTotalSetsLearned(userStatistics.getTotalSetsLearned() + 1);
            case "cardslearned" -> userStatistics.setTotalCardsLearned(userStatistics.getTotalCardsLearned() + 1);
            case "cardstolearnagain" -> userStatistics.setTotalCardsToLearnAgain(userStatistics.getTotalCardsToLearnAgain() + 1);
            case "basemethodmode" -> userStatistics.setBaseMethodModes(userStatistics.getBaseMethodModes() + 1);
            case "multiplechoicemode" -> userStatistics.setMultipleChoiceModes(userStatistics.getMultipleChoiceModes() + 1);
            case "truefalsemode" -> userStatistics.setTrueFalseModes(userStatistics.getTrueFalseModes() + 1);
            default -> {}
        }
    }
}
