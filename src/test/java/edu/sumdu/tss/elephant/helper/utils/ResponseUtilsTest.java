package edu.sumdu.tss.elephant.helper.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseUtilsTest {

    @Test
    void testSuccessResponse() {
        Map<String, String> expected = Map.of("status", "Ok", "message", "Success message");
        Map<String, String> result = (Map<String, String>) ResponseUtils.success("Success message");
        assertEquals(expected, result);
    }

    @Test
    void testErrorResponse() {
        Map<String, String> expected = Map.of("status", "Error", "message", "Error message");
        Map<String, String> result = (Map<String, String>) ResponseUtils.error("Error message");
        assertEquals(expected, result);
    }
}
