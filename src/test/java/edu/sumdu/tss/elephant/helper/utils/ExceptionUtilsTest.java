package edu.sumdu.tss.elephant.helper.utils;

import edu.sumdu.tss.elephant.helper.Keys;
import io.javalin.core.validation.ValidationError;
import io.javalin.core.validation.ValidationException;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ExceptionUtilsTest {
    @Test
    void shouldReturnValidationMessages() {
        // given
        ValidationError<Object> error = new ValidationError<>("test message", Map.of("1", "1"), "a1");
        ValidationException ex = new ValidationException(Map.of("key", List.of(error)));

        // when
        String result = ExceptionUtils.validationMessages(ex);

        // then
        assertEquals("<ul><li><b>key</b>&nbsp;test message </li></ul>", result);
    }

    @Test
    void shouldReturnFalseSQLUniqueException() {
        // given
        Throwable ex = new Throwable();

        // when
        boolean result = ExceptionUtils.isSQLUniqueException(ex);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueSQLUniqueException() {
        // given
        Throwable cause = new Throwable("duplicate key value violates unique constraint");
        Throwable ex = new Throwable(cause);

        // when
        boolean result = ExceptionUtils.isSQLUniqueException(ex);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnStacktrace() {
        // given
        Throwable cause = new Throwable("duplicate key value violates unique constraint");
        Throwable ex = new Throwable(cause);

        // when
        String result = ExceptionUtils.stacktrace(ex);

        // then
        assertTrue(result.contains("java.lang.Throwable"));
    }

    @Test
    void shouldWrapError() {
        // given
        ValidationException ex = new ValidationException(Map.of("key", List.of(new ValidationError<>("test message", Map.of("1", "1"), "a1"))));
        Context context = mock(Context.class);

        // when
        ExceptionUtils.wrapError(context, ex);

        // then
        verify(context).sessionAttribute(Keys.ERROR_KEY, "<ul><li><b>key</b>&nbsp;test message </li></ul>");
    }

    @Test
    void shouldWrapErrorWhenExceptionIsNotValidationException() {
        // given
        Exception ex = new Exception("test message");
        Context context = mock(Context.class);

        // when
        ExceptionUtils.wrapError(context, ex);

        // then
        verify(context).sessionAttribute(Keys.ERROR_KEY, ex.getMessage());
    }

}