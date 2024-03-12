package edu.sumdu.tss.elephant.middleware;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSRFTokenServiceTest {
    @Test
    void shouldGetSiteWideToken() {
        assertNotNull(CSRFTokenService.getSiteWideToken());
    }

    @Test
    void shouldGenerateToken() {
        String token = CSRFTokenService.generateToken("test");
        assertNotNull(token);
    }

    @Test
    void shouldValidateToken() {
        String token = CSRFTokenService.generateToken("test");
        assertTrue(CSRFTokenService.validateToken(token, "test"));
    }

    @Test
    void shouldNotValidateToken() {
        String token = CSRFTokenService.generateToken("test");
        assertFalse(CSRFTokenService.validateToken(token, "test2"));
    }
}