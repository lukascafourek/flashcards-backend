package cz.cvut.fel.cafoulu1.flashcards.backend.service.emails;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service for sending reset password email to user with token
 */
@Service
@RequiredArgsConstructor
public class ResetPasswordEmailImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private static final String SUPPORT_EMAIL_1 = "cafoulu1@fel.cvut.cz";

    private static final String SUPPORT_EMAIL_2 = "lukascafourek2002@gmail.com";

    @Override
    public void sendEmail(String token, String email) {
        String subject = "Password reset - Flashcards Web Learning App";
        String message = """
                Hello,
               \s
                We have received a request to reset your account password for Flashcards Web Learning App.
                If you did not submit this request, please ignore this email.
               \s
                To reset your password, please enter the following token in the reset password form:
               \s
                %s
               \s
                Do not share this token with anyone. Please do not reply to this email.
                If you have any questions or need help, please contact us at:
                - %s
                - %s
               \s
                Thank you,
                Flashcards Web Learning App Team
               \s""".formatted(token, SUPPORT_EMAIL_1, SUPPORT_EMAIL_2);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(sender);
        mailSender.send(mailMessage);
    }
}
