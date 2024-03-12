package edu.sumdu.tss.elephant.helper.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void shouldGetRandomAlphaString() {
        // given
        int targetStringLength = 10;

        // when
        String result = StringUtils.randomAlphaString(targetStringLength);

        // then
        assertTrue(result.matches("[a-zA-Z]{10}"));
    }

    @Test
    void shouldGetUuid() {
        // when
        String result = StringUtils.uuid();

        // then
        assertTrue(result.matches("[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"));
    }

    @Test
    void shouldReplaceLast() {
        // given
        String texto = "Test";
        String substituir = "t";
        String substituto = "T";

        // when
        String result = StringUtils.replaceLast(texto, substituir, substituto);

        // then
        assertEquals("TesT", result);
    }

    @Test
    void shouldDoNothingIfNotPresent() {
        // given
        String texto = "Test";
        String substituir = "L";
        String substituto = "l";

        // when
        String result = StringUtils.replaceLast(texto, substituir, substituto);

        // then
        assertEquals("Test", result);
    }
}