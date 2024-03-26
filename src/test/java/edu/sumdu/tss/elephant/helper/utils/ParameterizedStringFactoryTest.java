package edu.sumdu.tss.elephant.helper.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public record ParameterizedStringFactoryTest() {

    @Test
    void testToString() {
        var expected = "test";
        var parameterizedStringFactory = new ParameterizedStringFactory("test");
        var result = parameterizedStringFactory.toString();
        assertEquals(expected, result);
    }

    @Test
    void testNotEqualsToString() {
        var expected = "test1";
        var parameterizedStringFactory = new ParameterizedStringFactory("test");
        var result = parameterizedStringFactory.toString();
        assertNotEquals(expected, result);
    }

    @Test
    void testAddParameter() {
        var expected = new ParameterizedStringFactory.ParameterizedString("test")
                .addParameter("parameter1", "value1");
        var parameterizedStringFactory = new ParameterizedStringFactory("test");
        var result = parameterizedStringFactory.addParameter("parameter1", "value1");
        assertEquals(expected.toString(), result.toString());
    }
}

