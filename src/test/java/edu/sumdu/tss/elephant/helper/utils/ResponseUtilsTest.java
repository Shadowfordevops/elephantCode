package edu.sumdu.tss.elephant.helper.utils;

import edu.sumdu.tss.elephant.helper.ViewHelper;
import org.junit.jupiter.api.Test;

import io.javalin.http.Context;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResponseUtilsTest {

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnOkAnswerOnSuccess() {
        // given
        String message = "message";

        // when
        HashMap<String, String> result = (HashMap<String, String>) ResponseUtils.success(message);

        // then
        assertEquals("Ok", result.get("status"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnErrorAnswerOnError() {
        // given
        String message = "message";

        // when
        HashMap<String, String> result = (HashMap<String, String>) ResponseUtils.error(message);

        // then
        assertEquals("Error", result.get("status"));
    }

    @Test
    void shouldFlushFlash() {
        // given
        Context context = mock(Context.class);

        // when
        ResponseUtils.flush_flash(context);

        // then
        for (var key : ViewHelper.FLASH_KEY) {
            verify(context).sessionAttribute(key, null);
        }
    }
}