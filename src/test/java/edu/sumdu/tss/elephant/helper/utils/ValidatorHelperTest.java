package edu.sumdu.tss.elephant.helper.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorHelperTest {

    @Test
    void testIsValidPasswordValid() {
        String password = "Password1!";
        boolean result = ValidatorHelper.isValidPassword(password);
        assertTrue(result);
    }

    @Test
    void testIsValidPasswordInvalid() {
        String password = "password";
        boolean result = ValidatorHelper.isValidPassword(password);
        assertFalse(result);
    }

    @Test
    void testIsValidMailValid() {
        String mail = "test@mail.com";
        boolean result = ValidatorHelper.isValidMail(mail);
        assertTrue(result);
    }

    @Test
    void testIsValidMailInvalid() {
        String mail = "testmail.com";
        boolean result = ValidatorHelper.isValidMail(mail);
        assertFalse(result);
    }
}
