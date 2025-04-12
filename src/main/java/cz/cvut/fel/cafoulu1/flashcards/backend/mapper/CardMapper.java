package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Card;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {
    CardDto toDto(Card card);

    FullCardInfo toFullDto(Card card);

    Card createCard(CardRequest cardRequest);

    CardRequest createCardRequest(Card card);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Card partialUpdateCard(CardRequest cardRequest, @MappingTarget Card card);
}
