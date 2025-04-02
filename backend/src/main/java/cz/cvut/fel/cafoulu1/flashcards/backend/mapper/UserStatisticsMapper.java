package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.create.CreateUserStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserStatisticsMapper {
    UserStatistics toEntity(UserStatisticsDto userStatisticsDto);

    UserStatistics toEntityBasic(BasicUserStatisticsDto basicUserStatisticsDto);

    UserStatisticsDto toDto(UserStatistics userStatistics);

    BasicUserStatisticsDto toDtoBasic(UserStatistics userStatistics);

    UserStatistics createUserStatistics(CreateUserStatistics createUserStatistics);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserStatistics partialUpdate(UserStatisticsDto userStatisticsDto, @MappingTarget UserStatistics userStatistics);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserStatistics partialUpdateBasic(BasicUserStatisticsDto basicUserStatisticsDto, @MappingTarget UserStatistics userStatistics);

    @Named("incrementStatistic")
    default void incrementStatistic(UserStatistics userStatistics, String statistic) {
        switch (statistic.toLowerCase()) {
            case "totalsetslearned" -> userStatistics.setTotalSetsLearned(userStatistics.getTotalSetsLearned() + 1);
            case "totalcardslearned" -> userStatistics.setTotalCardsLearned(userStatistics.getTotalCardsLearned() + 1);
            case "totalcardstolearnagain" -> userStatistics.setTotalCardsToLearnAgain(userStatistics.getTotalCardsToLearnAgain() + 1);
            case "setscreated" -> userStatistics.setSetsCreated(userStatistics.getSetsCreated() + 1);
            case "cardscreated" -> userStatistics.setCardsCreated(userStatistics.getCardsCreated() + 1);
            case "basemethodmodes" -> userStatistics.setBaseMethodModes(userStatistics.getBaseMethodModes() + 1);
            case "multiplechoicemodes" -> userStatistics.setMultipleChoiceModes(userStatistics.getMultipleChoiceModes() + 1);
            case "truefalsemodes" -> userStatistics.setTrueFalseModes(userStatistics.getTrueFalseModes() + 1);
            default -> throw new IllegalArgumentException("User Statistic not found");
        }
    }
}
