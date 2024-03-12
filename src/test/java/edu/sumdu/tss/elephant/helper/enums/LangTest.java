package edu.sumdu.tss.elephant.helper.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LangTest {

    @Test
    void shouldGetEnByValue() {
        // given
        String value = "UK";

        // when
        Lang lang = Lang.byValue(value);

        // then
        assertEquals(Lang.UK, lang);
    }

    @Test
    void shouldThrowExceptionWhenValueNotExists() {
        // given
        String value = "RU";

        // when
        assertThrows(RuntimeException.class, () -> {
            // then
            Lang.byValue(value);
        });
    }
}