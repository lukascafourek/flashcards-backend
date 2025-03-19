package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.create.CreateCardSet;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardSetRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardSetMapper {
//    CardSet toEntity(CardSetDto cardSetDto);

    CardSet toEntityBasic(BasicCardSetDto basicCardSetDto);

//    @AfterMapping
//    default void linkCards(@MappingTarget CardSet cardSet) {
//        if (cardSet.getCards() == null) {
//            return;
//        }
//        cardSet.getCards().forEach(card -> card.setCardSet(cardSet));
//    }
//
//    @AfterMapping
//    default void linkSetStatistics(@MappingTarget CardSet cardSet) {
//        if (cardSet.getSetStatistics() == null) {
//            return;
//        }
//        cardSet.getSetStatistics().forEach(setStatistic -> setStatistic.setCardSet(cardSet));
//    }

//    CardSetDto toDto(CardSet cardSet);

    BasicCardSetDto toDtoBasic(CardSet cardSet);

    CardSet createCardSet(CardSetRequest cardSetRequest);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    CardSet partialUpdate(CardSetDto cardSetDto, @MappingTarget CardSet cardSet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CardSet partialUpdateBasic(BasicCardSetDto basicCardSetDto, @MappingTarget CardSet cardSet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CardSet partialUpdateCardSet(CardSetRequest cardSetRequest, @MappingTarget CardSet cardSet);
}
