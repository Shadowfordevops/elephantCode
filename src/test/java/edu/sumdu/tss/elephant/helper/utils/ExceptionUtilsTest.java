package edu.sumdu.tss.elephant.helper.utils;

import io.javalin.core.validation.ValidationError;
import io.javalin.core.validation.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionUtilsTest {

    @Test
    void testValidationMessages() {
        String expected = "<ul><li><b>test</b>&nbsp;test </li></ul>";
        ValidationError error = new ValidationError<>("test", new HashMap<>(), "test");
        ValidationException exception = new ValidationException(Map.of("test", Arrays.asList(error)));
        String message = ExceptionUtils.validationMessages(exception);
        assertEquals(expected, message);
    }

    @Test
    void testIsSQLUniqueExceptionTrue() {
        Throwable exception = new Throwable("some message duplicate key value violates unique constraint some message");
        boolean result = ExceptionUtils.isSQLUniqueException(exception);
        assertTrue(result);
    }

    @Test
    void testIsSQLUniqueExceptionFalse() {
        Throwable exception = new Throwable("some message");
        boolean result = ExceptionUtils.isSQLUniqueException(exception);
        assertFalse(result);
    }

    @Test
    void testStacktrace() {
        Throwable exception = new Throwable("some message");
        String result = ExceptionUtils.stacktrace(exception);
        assertTrue(result.contains("some message"));
        assertTrue(result.contains("at edu.sumdu.tss.elephant.helper.utils.ExceptionUtilsTest.testStacktrace"));
    }
}
