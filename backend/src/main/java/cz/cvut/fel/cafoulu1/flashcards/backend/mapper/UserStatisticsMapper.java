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

    @Named("getWantedStatistic")
    default Integer getWantedUserStatistic(UserStatistics userStatistics, String wantedStatistic) {
        return switch (wantedStatistic.toLowerCase()) {
            case "totalsetslearned" -> userStatistics.getTotalSetsLearned();
            case "totalcardslearned" -> userStatistics.getTotalCardsLearned();
            case "totalcardstolearnagain" -> userStatistics.getTotalCardsToLearnAgain();
            case "setscreated" -> userStatistics.getSetsCreated();
            case "cardscreated" -> userStatistics.getCardsCreated();
            case "basemethodmodes" -> userStatistics.getBaseMethodModes();
            case "multiplechoicemodes" -> userStatistics.getMultipleChoiceModes();
            case "connectionmodes" -> userStatistics.getConnectionModes();
            default -> throw new IllegalArgumentException("Invalid statistic: " + wantedStatistic);
        };
    }
}
