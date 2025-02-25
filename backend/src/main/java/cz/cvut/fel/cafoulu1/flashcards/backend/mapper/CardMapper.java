package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicCardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.create.CreateCard;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Card;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {CardSetMapper.class})
public interface CardMapper {
    Card toEntity(CardDto cardDto);

    Card toEntityBasic(BasicCardDto cardDto);

    CardDto toDto(Card card);

    BasicCardDto toDtoBasic(Card card);

    Card createCard(CreateCard createCard);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Card partialUpdate(CardDto cardDto, @MappingTarget Card card);
}
