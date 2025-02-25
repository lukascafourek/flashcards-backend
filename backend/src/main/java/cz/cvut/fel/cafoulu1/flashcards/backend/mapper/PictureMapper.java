package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.PictureDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicPictureDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.dto.create.CreatePicture;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Picture;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {CardMapper.class})
public interface PictureMapper {
    Picture toEntity(PictureDto pictureDto);

    Picture toEntityBasic(BasicPictureDto basicPictureDto);

    PictureDto toDto(Picture picture);

    BasicPictureDto toDtoBasic(Picture picture);

    Picture createPicture(CreatePicture createPicture);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Picture partialUpdate(PictureDto pictureDto, @MappingTarget Picture picture);
}
