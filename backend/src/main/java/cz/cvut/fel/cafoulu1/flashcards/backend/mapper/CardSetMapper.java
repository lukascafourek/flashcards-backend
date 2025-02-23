package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, CardMapper.class, SetStatisticsMapper.class})
public interface CardSetMapper {
    CardSet toEntity(CardSetDto cardSetDto);

    @AfterMapping
    default void linkCards(@MappingTarget CardSet cardSet) {
        cardSet.getCards().forEach(card -> card.setCardSet(cardSet));
    }

    @AfterMapping
    default void linkSetStatistics(@MappingTarget CardSet cardSet) {
        cardSet.getSetStatistics().forEach(setStatistic -> setStatistic.setCardSet(cardSet));
    }

    CardSetDto toDto(CardSet cardSet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CardSet partialUpdate(CardSetDto cardSetDto, @MappingTarget CardSet cardSet);
}
