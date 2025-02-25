package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserStatisticsDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.create.CreateUserStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.UserStatistics;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface UserStatisticsMapper {
    UserStatistics toEntity(UserStatisticsDto userStatisticsDto);

    UserStatistics toEntityBasic(BasicUserStatisticsDto basicUserStatisticsDto);

    UserStatisticsDto toDto(UserStatistics userStatistics);

    BasicUserStatisticsDto toDtoBasic(UserStatistics userStatistics);

    UserStatistics createUserStatistics(CreateUserStatistics createUserStatistics);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserStatistics partialUpdate(UserStatisticsDto userStatisticsDto, @MappingTarget UserStatistics userStatistics);
}
