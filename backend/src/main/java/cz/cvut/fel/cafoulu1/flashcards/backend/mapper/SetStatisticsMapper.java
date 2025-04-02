package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.SetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicSetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.create.CreateSetStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SetStatisticsMapper {
    SetStatistics toEntity(SetStatisticsDto setStatisticsDto);

    SetStatistics toEntityBasic(BasicSetStatisticsDto basicSetStatisticsDto);

    SetStatisticsDto toDto(SetStatistics setStatistics);

    BasicSetStatisticsDto toDtoBasic(SetStatistics setStatistics);

    SetStatistics createSetStatistics(CreateSetStatistics createSetStatistics);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SetStatistics partialUpdate(SetStatisticsDto setStatisticsDto, @MappingTarget SetStatistics setStatistics);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SetStatistics partialUpdateBasic(BasicSetStatisticsDto basicSetStatisticsDto, @MappingTarget SetStatistics setStatistics);

    @Named("incrementStatistic")
    default void incrementStatistic(SetStatistics setStatistics, String statistic) {
        switch (statistic.toLowerCase()) {
            case "setslearned" -> setStatistics.setSetsLearned(setStatistics.getSetsLearned() + 1);
            case "cardslearned" -> setStatistics.setCardsLearned(setStatistics.getCardsLearned() + 1);
            case "cardstolearnagain" -> setStatistics.setCardsToLearnAgain(setStatistics.getCardsToLearnAgain() + 1);
            case "basemethodmode" -> setStatistics.setBaseMethodMode(setStatistics.getBaseMethodMode() + 1);
            case "multiplechoicemode" -> setStatistics.setMultipleChoiceMode(setStatistics.getMultipleChoiceMode() + 1);
            case "truefalsemode" -> setStatistics.setTrueFalseMode(setStatistics.getTrueFalseMode() + 1);
            default -> throw new IllegalArgumentException("Set Statistic not found");
        }
    }
}
