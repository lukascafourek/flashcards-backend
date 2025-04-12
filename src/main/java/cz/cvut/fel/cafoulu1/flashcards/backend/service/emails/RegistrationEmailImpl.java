package cz.cvut.fel.cafoulu1.flashcards.backend.service.emails;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service for sending a registration email to a new user.
 */
@Service
public class RegistrationEmailImpl extends AbstractEmail implements EmailService {
    public RegistrationEmailImpl(JavaMailSender mailSender) {
        super(mailSender);
    }

    @Override
    public void sendEmail(String username, String email) {
        String subject = "Welcome to Flashcards Web Learning App";
        String message = """
                Hello %s,
               \s
                Thank you for signing up for Flashcards Web Learning App!
                We are happy that you have joined our community and hope that our app will help you learn and practice your knowledge.
               \s
                ✨ What can you do in our app?
                ✅ Create your own flashcard sets
                ✅ Explore flashcard sets created by others
                ✅ Practice in three different modes
                ✅ Track your learning progress
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
               \s""".formatted(username, getSUPPORT_EMAIL_1(), getSUPPORT_EMAIL_2());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(getSender());
        getMailSender().send(mailMessage);
    }
}
