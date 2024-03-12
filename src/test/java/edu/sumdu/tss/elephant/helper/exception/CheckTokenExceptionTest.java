package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckTokenExceptionTest {

    @Test
    void shouldConstructDefault() {
        // when
        CheckTokenException exception = new CheckTokenException();

        // then
        assertNull(exception.getCause());
    }

}