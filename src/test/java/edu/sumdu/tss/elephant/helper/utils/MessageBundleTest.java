package edu.sumdu.tss.elephant.helper.utils;

import dev.akkinoc.util.YamlResourceBundle;
import edu.sumdu.tss.elephant.helper.Keys;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageBundleTest {
    private static final MockedStatic<ResourceBundle> resourceBundle = mockStatic(ResourceBundle.class);
    private static final MockedStatic<Keys> keys = mockStatic(Keys.class);

    @AfterAll
    static void tearDown() {
        resourceBundle.close();
    }

    @Test
    void shouldCreateMessageBundleFromLanguageTag() {
        // given
        String languageTag = "en";
        ResourceBundle res = mock(ResourceBundle.class);

        // when
        resourceBundle.when(() -> ResourceBundle.getBundle("i18n/messages", new Locale(languageTag), YamlResourceBundle.Control.INSTANCE)).thenReturn(res);
        MessageBundle messageBundle = new MessageBundle(languageTag);

        // then
        assertNotNull(messageBundle);
    }

    @Test
    void shouldCreateMesasgeBundleFromDefaultLanguage() {

        // given
        ResourceBundle res = mock(ResourceBundle.class);

        // when
        keys.when(() -> Keys.get("DEFAULT_LANG")).thenReturn("en");
        resourceBundle.when(() -> ResourceBundle.getBundle("i18n/messages", new Locale("en"), YamlResourceBundle.Control.INSTANCE)).thenReturn(res);
        MessageBundle messageBundle = new MessageBundle("en");

        // then
        assertNotNull(messageBundle);
    }

    @Test
    void shouldGetMessage() {
        // given
        ResourceBundle res = mock(ResourceBundle.class);
        String languageTag = "en";
        resourceBundle.when(() -> ResourceBundle.getBundle("i18n/messages", new Locale(languageTag), YamlResourceBundle.Control.INSTANCE)).thenReturn(res);
        MessageBundle messageBundle = new MessageBundle(languageTag);

        String message = "message";

        // when
        when(res.getString(message)).thenReturn("result");

        // then
        assertEquals("result", messageBundle.get(message));
    }
    @Test
    void shouldNotGetMessage() {
        // given
        ResourceBundle res = mock(ResourceBundle.class);
        String languageTag = "en";
        resourceBundle.when(() -> ResourceBundle.getBundle("i18n/messages", new Locale(languageTag), YamlResourceBundle.Control.INSTANCE)).thenReturn(res);
        MessageBundle messageBundle = new MessageBundle(languageTag);

        String message = "message";

        // when
        when(res.getString(message)).thenThrow(RuntimeException.class);

        // then
        assertEquals("I18n not found:" + message, messageBundle.get(message));
    }

    @Test
    void shouldGetParam() {
        // given
        ResourceBundle res = mock(ResourceBundle.class);
        String languageTag = "en";
        resourceBundle.when(() -> ResourceBundle.getBundle("i18n/messages", new Locale(languageTag), YamlResourceBundle.Control.INSTANCE)).thenReturn(res);
        MessageBundle messageBundle = new MessageBundle(languageTag);

        String key = "key";

        // when
        when(res.getString(key)).thenReturn("test");
        String result = messageBundle.get(key, "test");

        // then
        assertEquals("test", result);
    }
}