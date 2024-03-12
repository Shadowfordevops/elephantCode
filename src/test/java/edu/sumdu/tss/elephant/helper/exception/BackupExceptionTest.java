package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BackupExceptionTest {
    @Test
    void shouldConstructWithCause() {
        // given
        Exception cause = new Exception();

        // when
        BackupException exception = new BackupException(cause);

        // then
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldConstructWithMessage() {
        // given
        String message = "message";

        // when
        BackupException exception = new BackupException(message);

        // then
        assertEquals("message", exception.getMessage());
    }

    @Test
    void shouldConstructWithMessageAndCause() {
        // given
        String message = "message";
        Exception cause = new Exception();

        // when
        BackupException exception = new BackupException(message, cause);

        // then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}