package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpExceptionTest {

    @Test
    void shouldGetCode() {
        // given
        HttpException exception = new HttpException();

        // when
        Integer code = exception.getCode();

        // then
        assertEquals(500, code);
    }

    @Test
    void shouldGetIcon() {
        // given
        HttpException exception = new HttpException();

        // when
        String icon = exception.getIcon();

        // then
        assertEquals("bug", icon);
    }

    @Test
    void shouldConstructWithCause() {
        // given
        Exception cause = new Exception();

        // when
        HttpException exception = new HttpException(cause);

        // then
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldConstructWithMessage() {
        // given
        String message = "message";

        // when
        HttpException exception = new HttpException(message);

        // then
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldConstructWithMessageAndCause() {
        // given
        Exception cause = new Exception();
        String message = "message";

        // when
        HttpException exception = new HttpException(message, cause);

        // then
        assertEquals(message, exception.getMessage());
    }
}