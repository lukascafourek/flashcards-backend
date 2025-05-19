package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.CardSetDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.CardSetRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.request.FilterCardSetsRequest;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.response.FullCardSetInfo;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardSetMapper {
    FullCardSetInfo toFullDto(CardSet cardSet);

    CardSetDto toDto(CardSet cardSet);

    CardSet createCardSet(CardSetRequest cardSetRequest);

    CardSetRequest createCardSetRequest(CardSet cardSet);

    FilterCardSetsRequest createFilterCardSetsRequest(String category, String search, Boolean mine, Boolean favorite, UUID userId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CardSet partialUpdateCardSet(CardSetRequest cardSetRequest, @MappingTarget CardSet cardSet);
}
