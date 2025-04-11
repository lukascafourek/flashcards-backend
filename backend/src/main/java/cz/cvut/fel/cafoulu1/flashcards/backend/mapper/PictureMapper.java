package cz.cvut.fel.cafoulu1.flashcards.backend.mapper;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.Card;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.Picture;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PictureMapper {
    Picture createPicture(byte[] picture, Card card);
}
