package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.TokenDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicTokenDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.create.CreateToken;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Token;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TokenMapper {
    Token toEntity(TokenDto tokenDto);

    Token toEntityBasic(BasicTokenDto basicTokenDto);

    TokenDto toDto(Token token);

    BasicTokenDto toDtoBasic(Token token);

    Token createToken(CreateToken createToken);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Token partialUpdate(TokenDto tokenDto, @MappingTarget Token token);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Token partialUpdateBasic(BasicTokenDto basicTokenDto, @MappingTarget Token token);
}
