package edu.sumdu.tss.elephant.helper.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTest {

    @Test
    void testRandomAlphaString() {
        int expectedLength = 10;
        String result = StringUtils.randomAlphaString(10);
        assertEquals(result.length(), expectedLength);
    }

    @Test
    void testUuid() {
        int expectedLength = 36;
        String result = StringUtils.uuid();
        assertEquals(result.length(), expectedLength);
    }

    @Test
    void testReplaceLast() {
        String expected = "some text text word";
        String result = StringUtils.replaceLast("some text text text", "text", "word");
        assertEquals(expected, result);
    }

    @Test
    void testNotReplaceLast() {
        String expected = "some word word word";
        String result = StringUtils.replaceLast("some word word word", "text", "parameter");
        assertEquals(expected, result);
    }
}
