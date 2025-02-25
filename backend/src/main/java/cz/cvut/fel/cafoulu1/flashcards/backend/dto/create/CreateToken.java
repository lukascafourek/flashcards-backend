package cz.cvut.fel.cafoulu1.flashcards.backend.dto.create;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class CreateToken implements Serializable {
    BasicUserDto user;
    String resetToken;
    LocalDateTime expirationDate;
}
