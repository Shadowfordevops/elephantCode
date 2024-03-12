package edu.sumdu.tss.elephant.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    void shouldGetKey() {
        // given
        Pair<String, String> pair = new Pair<>("key", "value");

        // when
        String key = pair.getKey();

        // then
        assertEquals("key", key);
    }

    @Test
    void shouldGetValue() {
        // given
        Pair<String, String> pair = new Pair<>("key", "value");

        // when
        String value = pair.getValue();

        // then
        assertEquals("value", value);
    }

    @Test
    void shouldSetDefaultValues() {
        // given
        Pair<String, String> pair = new Pair<>();

        // when
        String key = pair.getKey();
        String value = pair.getValue();

        // then
        assertNull(key);
        assertNull(value);
    }
}