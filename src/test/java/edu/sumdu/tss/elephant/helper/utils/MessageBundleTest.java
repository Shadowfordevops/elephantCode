package edu.sumdu.tss.elephant.helper.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageBundleTest {

    @Test
    void testGet() {
        String expected = "Sign me in, please";
        MessageBundle messageBundle = new MessageBundle("en");
        String result = messageBundle.get("login.SignIn");
        assertEquals(expected, result);
    }

    @Test
    void testGetWithNoMessage() {
        String expected = "I18n not found:test.test.test";
        MessageBundle messageBundle = new MessageBundle("en");
        String result = messageBundle.get("test.test.test");
        assertEquals(expected, result);
    }

    @Test
    void testGetWithArgs() {
        String expected = "Hi, Oleksandr Borovyk!";
        MessageBundle messageBundle = new MessageBundle("en");
        String result = messageBundle.get("dashboard.jumbotron.title", "Oleksandr Borovyk");
        assertEquals(expected, result);
    }
}
