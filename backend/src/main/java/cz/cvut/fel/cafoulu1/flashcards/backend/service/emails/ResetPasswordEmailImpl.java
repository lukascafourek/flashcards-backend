package cz.cvut.fel.cafoulu1.flashcards.backend.service.emails;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service for sending reset password email to user with token
 */
@Service
public class ResetPasswordEmailImpl extends AbstractEmail implements EmailService {
    public ResetPasswordEmailImpl(JavaMailSender mailSender) {
        super(mailSender);
    }

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
               \s""".formatted(token, getSUPPORT_EMAIL_1(), getSUPPORT_EMAIL_2());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(getSender());
        getMailSender().send(mailMessage);
    }
}
