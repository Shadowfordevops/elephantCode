package edu.sumdu.tss.elephant.middleware;

import edu.sumdu.tss.elephant.helper.exception.CheckTokenException;
import io.javalin.http.Context;
import io.javalin.http.util.ContextUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CSRFFilterTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Test
    void shouldGenerate() {
        // given
        String mockCSRFToken = "mockCSRFToken";
        Context context = spy(ContextUtil.init(request, response));

        // when
        when(request.getMethod()).thenReturn("GET");
        doReturn(null).when(context).sessionAttribute("SessionID");
        doNothing().when(context).sessionAttribute(anyString(), anyString());

        try (MockedStatic<CSRFTokenService> mockedService = mockStatic(CSRFTokenService.class)) {
            mockedService.when(() -> CSRFTokenService.generateToken(anyString())).thenReturn(mockCSRFToken);

            CSRFFilter.generate(context);

            // then
            verify(context).sessionAttribute(eq("csrf"), eq(mockCSRFToken));
        }
    }

    @Test
    void shouldNotCheckWhenGetMethod() {
        // given
        Context context = spy(ContextUtil.init(request, response));

        // when
        when(request.getMethod()).thenReturn("GET");

        try (MockedStatic<CSRFTokenService> mockedService = mockStatic(CSRFTokenService.class)) {
            mockedService.when(() -> CSRFTokenService.validateToken(anyString(), anyString())).thenReturn(true);

            CSRFFilter.check(context);

            // then
            verifyNoMoreInteractions(context);
        }
    }

    @Test
    void shouldThrowWhenNoCSRFToken() {
        // given
        Context context = spy(ContextUtil.init(request, response));

        // when
        when(request.getMethod()).thenReturn("POST");
        doReturn(null).when(context).formParam("_csrf");
        doReturn(null).when(context).sessionAttribute("SessionID");

        try (MockedStatic<CSRFTokenService> mockedService = mockStatic(CSRFTokenService.class)) {
            mockedService.when(() -> CSRFTokenService.validateToken(anyString(), anyString())).thenReturn(true);

            // then
            assertThrows(CheckTokenException.class, () -> CSRFFilter.check(context));
        }
    }

    @Test
    void shouldThrowWhenInvalidCSRFToken() {
        // given
        Context context = spy(ContextUtil.init(request, response));

        // when
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader("X-CSRF-TOKEN")).thenReturn("invalid");
        doReturn("invalid").when(context).formParam("_csrf");
        doReturn(null).when(context).sessionAttribute("SessionID");

        try (MockedStatic<CSRFTokenService> mockedService = mockStatic(CSRFTokenService.class)) {
            mockedService.when(() -> CSRFTokenService.validateToken(anyString(), anyString())).thenReturn(false);

            // then
            assertThrows(CheckTokenException.class, () -> CSRFFilter.check(context));
        }
    }

    @Test
    void shouldCheck() {
        // given
        Context context = spy(ContextUtil.init(request, response));

        // when
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader("X-CSRF-TOKEN")).thenReturn("valid");
        doReturn("valid").when(context).formParam("_csrf");
        doReturn(null).when(context).sessionAttribute("SessionID");

        try (MockedStatic<CSRFTokenService> mockedService = mockStatic(CSRFTokenService.class)) {
            mockedService.when(() -> CSRFTokenService.validateToken(anyString(), anyString())).thenReturn(true);

            // then
            assertDoesNotThrow(() -> CSRFFilter.check(context));
        }
    }


}