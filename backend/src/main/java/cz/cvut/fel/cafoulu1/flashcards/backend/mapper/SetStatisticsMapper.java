package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.SetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicSetStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.create.CreateSetStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, CardSetMapper.class})
public interface SetStatisticsMapper {
    SetStatistics toEntity(SetStatisticsDto setStatisticsDto);

    SetStatistics toEntityBasic(BasicSetStatisticsDto basicSetStatisticsDto);

    SetStatisticsDto toDto(SetStatistics setStatistics);

    BasicSetStatisticsDto toDtoBasic(SetStatistics setStatistics);

    SetStatistics createSetStatistics(CreateSetStatistics createSetStatistics);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SetStatistics partialUpdate(SetStatisticsDto setStatisticsDto, @MappingTarget SetStatistics setStatistics);
}
