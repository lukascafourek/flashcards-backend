package cz.cvut.fel.cafoulu1.flashcards.backend.service.emails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Abstract class for sending emails.
 * Contains common properties for email services and getter methods for them.
 */
@Service
@RequiredArgsConstructor
@Getter
public abstract class AbstractEmail {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${support.email.one}")
    private String SUPPORT_EMAIL_1;

    @Value("${support.email.two}")
    private String SUPPORT_EMAIL_2;
}
