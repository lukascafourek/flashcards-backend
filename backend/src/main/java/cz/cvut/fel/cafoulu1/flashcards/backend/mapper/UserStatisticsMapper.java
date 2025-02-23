package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface UserStatisticsMapper {
    UserStatistics toEntity(UserStatisticsDto userStatisticsDto);

    UserStatisticsDto toDto(UserStatistics userStatistics);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserStatistics partialUpdate(UserStatisticsDto userStatisticsDto, @MappingTarget UserStatistics userStatistics);
}
