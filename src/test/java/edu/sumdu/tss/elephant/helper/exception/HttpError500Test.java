package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpError500Test {
    @Test
    void shouldGetCode() {
        // given
        HttpError500 exception = new HttpError500();

        // when
        Integer code = exception.getCode();

        // then
        assertEquals(500, code);
    }

    @Test
    void shouldConstructWithCause() {
        // given
        Exception cause = new Exception();

        // when
        HttpError500 exception = new HttpError500(cause);

        // then
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldConstructWithMessage() {
        // given
        String message = "message";

        // when
        HttpError500 exception = new HttpError500(message);

        // then
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldConstructWithMessageAndCause() {
        // given
        Exception cause = new Exception();
        String message = "message";

        // when
        HttpError500 exception = new HttpError500(message, cause);

        // then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}