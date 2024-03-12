package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpError400Test {

    @Test
    void shouldGetCode() {
        // given
        HttpError400 exception = new HttpError400();

        // when
        Integer code = exception.getCode();

        // then
        assertEquals(400, code);
    }

    @Test
    void shouldConstructWithCause() {
        // given
        Exception cause = new Exception();

        // when
        HttpError400 exception = new HttpError400(cause);

        // then
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldConstructWithMessage() {
        // given
        String message = "message";

        // when
        HttpError400 exception = new HttpError400(message);

        // then
        assertEquals(message, exception.getMessage());
    }
}