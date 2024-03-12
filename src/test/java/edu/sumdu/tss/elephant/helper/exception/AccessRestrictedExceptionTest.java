package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccessRestrictedExceptionTest {
    @Test
    void shouldConstructDefault() {
        // when
        AccessRestrictedException exception = new AccessRestrictedException();

        // then
        assertNull(exception.getCause());
    }

    @Test
    void shouldConstructWithCause() {
        // given
        Exception cause = new Exception();

        // when
        AccessRestrictedException exception = new AccessRestrictedException(cause);

        // then
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldConstructWithMessage() {
        // given
        String message = "message";

        // when
        AccessRestrictedException exception = new AccessRestrictedException(message);

        // then
        assertEquals("message", exception.getMessage());
    }

}