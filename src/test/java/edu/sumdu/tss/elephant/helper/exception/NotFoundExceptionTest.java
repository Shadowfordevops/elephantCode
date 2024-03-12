package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {
    @Test
    void shouldConstructWithMessage() {
        // given
        String message = "message";

        // when
        NotFoundException exception = new NotFoundException(message);

        // then
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldGetCode() {
        // when
        NotFoundException exception = new NotFoundException("message");

        // then
        assertEquals(404, exception.getCode());
    }
}