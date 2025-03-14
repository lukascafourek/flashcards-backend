package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import cz.cvut.fel.cafoulu1.flashcards.backend.dto.basic.BasicUserDto;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.TokenServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.UserServiceImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.emails.ResetPasswordEmailImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private TokenServiceImpl tokenService;

    @Mock
    private ResetPasswordEmailImpl resetPasswordEmail;

    @InjectMocks
    private TokenController tokenController;

    public TokenControllerTest() {
        MockitoAnnotations.openMocks(this);
    }


}
