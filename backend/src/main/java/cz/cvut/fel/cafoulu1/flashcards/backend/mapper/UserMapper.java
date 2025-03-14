package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.UserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.create.CreateUser;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.UpdateUserRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    User toEntityBasic(BasicUserDto basicUserDto);

//    @AfterMapping
//    default void linkCardSets(@MappingTarget User user) {
//        if (user.getCardSets() == null) {
//            return;
//        }
//        user.getCardSets().forEach(cardSet -> cardSet.setUser(user));
//    }
//
//    @AfterMapping
//    default void linkSetStatistics(@MappingTarget User user) {
//        if (user.getSetStatistics() == null) {
//            return;
//        }
//        user.getSetStatistics().forEach(setStatistic -> setStatistic.setUser(user));
//    }

    UserDto toDto(User user);

    BasicUserDto toDtoBasic(User user);

    User createUser(CreateUser createUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdateBasic(BasicUserDto basicUserDto, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdateUser(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}
