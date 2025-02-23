package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {SetStatisticsMapper.class, CardSetMapper.class})
public interface UserMapper {
    User toEntity(UserDto userDto);

    @AfterMapping
    default void linkCardSets(@MappingTarget User user) {
        user.getCardSets().forEach(cardSet -> cardSet.setUser(user));
    }

    @AfterMapping
    default void linkSetStatistics(@MappingTarget User user) {
        user.getSetStatistics().forEach(setStatistic -> setStatistic.setUser(user));
    }

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}
