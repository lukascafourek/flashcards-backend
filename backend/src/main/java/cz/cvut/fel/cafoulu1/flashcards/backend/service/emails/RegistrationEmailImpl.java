package cz.cvut.fel.cafoulu1.flashcards.backend.service.emails;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service for sending a registration email to a new user.
 */
@Service
@RequiredArgsConstructor
public class RegistrationEmailImpl implements RegistrationEmail {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private static final String SUPPORT_EMAIL_1 = "cafoulu1@fel.cvut.cz";

    private static final String SUPPORT_EMAIL_2 = "lukascafourek2002@gmail.com";

    @Override
    public void sendEmail(User user) {
        String subject = "Welcome to Flashcards Web Learning App";
        String message = """
                Hello %s,
               \s
                Thank you for signing up for Flashcards Web Learning App!
                We are happy that you have joined our community and hope that our app will help you learn and practice your knowledge.
               \s
                ✨ What can you do in our app?
                ✅ Create your own flashcard sets
                ✅ Practice in a fun and effective way
                ✅ Track your progress
               \s
                Please do not reply to this email.
                If you have any questions or need help, feel free to contact us at:
                - %s
                - %s
               \s
                We wish you much success in your studies!
               \s
                Thank you,
                Flashcards Web Learning App Team
               \s""".formatted(user.getUsername(), SUPPORT_EMAIL_1, SUPPORT_EMAIL_2);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(sender);
        mailSender.send(mailMessage);
    }
}
