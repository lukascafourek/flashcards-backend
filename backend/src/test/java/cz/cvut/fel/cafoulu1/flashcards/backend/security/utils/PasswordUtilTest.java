package cz.cvut.fel.cafoulu1.flashcards.backend.security.utils;

import cz.cvut.fel.cafoulu1.flashcards.backend.security.utils.PasswordUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    void hashPassword_shouldReturnNonNullHash() {
        String plainPassword = "password123";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        assertNotNull(hashedPassword);
    }

    @Test
    void checkPassword_shouldReturnTrueForCorrectPassword() {
        String plainPassword = "password123";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        assertTrue(PasswordUtil.checkPassword(plainPassword, hashedPassword));
    }

    @Test
    void checkPassword_shouldReturnFalseForIncorrectPassword() {
        String plainPassword = "password123";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        assertFalse(PasswordUtil.checkPassword("wrongPassword", hashedPassword));
    }

    @Test
    void hashPassword_shouldReturnDifferentHashesForDifferentPasswords() {
        String plainPassword1 = "password123";
        String plainPassword2 = "password456";
        String hashedPassword1 = PasswordUtil.hashPassword(plainPassword1);
        String hashedPassword2 = PasswordUtil.hashPassword(plainPassword2);
        assertNotEquals(hashedPassword1, hashedPassword2);
    }

    @Test
    void hashPassword_shouldReturnDifferentHashesForSamePassword() {
        String plainPassword = "password123";
        String hashedPassword1 = PasswordUtil.hashPassword(plainPassword);
        String hashedPassword2 = PasswordUtil.hashPassword(plainPassword);
        assertNotEquals(hashedPassword1, hashedPassword2);
    }
}
