package edu.sumdu.tss.elephant.helper;

import edu.sumdu.tss.elephant.controller.AbstractController;
import edu.sumdu.tss.elephant.helper.exception.HttpException;
import edu.sumdu.tss.elephant.helper.utils.ExceptionUtils;
import edu.sumdu.tss.elephant.model.Database;
import edu.sumdu.tss.elephant.model.DatabaseService;
import edu.sumdu.tss.elephant.model.User;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewHelperTest {
    @Mock
    private Context context;

    @Test
    void shouldRenderErrorFromException() {
        // given
        HttpException exception = new HttpException("test");

        try (MockedStatic<ExceptionUtils> exceptionUtils = mockStatic(ExceptionUtils.class);
             MockedStatic<AbstractController> abstractController = mockStatic(AbstractController.class);
             MockedStatic<Keys> keys = mockStatic(Keys.class)) {
            // when
            exceptionUtils.when(() -> ExceptionUtils.stacktrace(exception)).thenReturn("stacktrace");
            abstractController.when(() -> AbstractController.currentModel(context)).thenReturn(new HashMap<>());
            keys.when(Keys::isProduction).thenReturn(false);

            ViewHelper.userError(exception, context);

            // then
            verify(context).status(exception.getCode());
            verify(context).render(eq("/velocity/error.vm"), any());
        }
    }

    @Test
    void shouldRenderError() {
        // given
        try (MockedStatic<AbstractController> abstractController = mockStatic(AbstractController.class);
             MockedStatic<Keys> keys = mockStatic(Keys.class)) {
            // when
            abstractController.when(() -> AbstractController.currentModel(context)).thenReturn(new HashMap<>());
            keys.when(Keys::isProduction).thenReturn(false);

            ViewHelper.userError(context, 500, "test", "test", "stacktrace");

            // then
            verify(context).status(500);
            verify(context).render(eq("/velocity/error.vm"), any());
        }
    }

    @Test
    void shouldRenderErrorWithoutStacktrace() {
        // given
        try (MockedStatic<AbstractController> abstractController = mockStatic(AbstractController.class);
             MockedStatic<Keys> keys = mockStatic(Keys.class)) {
            // when
            abstractController.when(() -> AbstractController.currentModel(context)).thenReturn(new HashMap<>());
            keys.when(Keys::isProduction).thenReturn(true);

            ViewHelper.userError(context, 500, "test", "test", null);

            // then
            verify(context).status(500);
            verify(context).render(eq("/velocity/error.vm"), any());
        }
    }

    @Test
    void shouldGetBreadcrumb() {
        // given

        // when
        when(context.sessionAttribute(Keys.BREADCRUMB_KEY)).thenReturn(new ArrayList<>());
        List<String> result = ViewHelper.breadcrumb(context);

        // then
        verifyNoMoreInteractions(context);
        assertEquals(0, result.size());
    }

    @Test
    void shouldCreateBreadcrumb() {
        // given

        // when
        List<String> result = ViewHelper.breadcrumb(context);

        // then
        verify(context).sessionAttribute(Keys.BREADCRUMB_KEY, result);
        assertTrue(result.contains("<a href='/home'><ion-icon name=\"home-outline\"></ion-icon></a>"));
        assertEquals(1, result.size());
    }

    @Test
    void shouldCleanupSession() {
        // given

        // when
        ViewHelper.cleanupSession(context);

        // then
        verify(context).sessionAttribute(Keys.MODEL_KEY, null);
        verify(context).sessionAttribute(Keys.DB_KEY, null);
        verify(context).sessionAttribute(Keys.BREADCRUMB_KEY, null);
    }

    @Test
    void shouldGetPager() {
        // given
        int totalPage = 3;
        int currentPage = 1;

        // when
        String result = ViewHelper.pager(totalPage, currentPage);

        // then
        assertEquals("""
                        <nav>
                        <ul class="pagination"><li class="page-item active"><a class="page-link" href="#">1</a></li>
                        <li class="page-item"><a class="page-link" href="?offset=2">2</a></li>
                        </ul>
                        </nav>""",
                result);
    }

    @Test
    void shouldGetSoftError() {
        // given
        String message = "test";

        // when

        ViewHelper.softError(message, context);

        // then
        verify(context).sessionAttribute("error", message);
        verify(context).redirect("/");
    }

    @Test
    void shouldRedirectBack() {
        // given

        // when
        when(context.header("Referer")).thenReturn("/home");

        ViewHelper.redirectBack(context);

        // then
        verify(context).redirect("/home");
    }

    @Test
    void shouldSetDefaultVariables() {
        // given
        User user = new User();
        user.setLanguage("en");
        List<String> crumbs = new ArrayList<>();
        Database database = new Database();

        // when
        doNothing().when(context).sessionAttribute(any(), any());
        when(context.sessionAttribute("csrf")).thenReturn("csrf");
        when(context.sessionAttribute(Keys.SESSION_CURRENT_USER_KEY)).thenReturn(user);
        when(context.path()).thenReturn("database/test");
        when(context.sessionAttribute(Keys.BREADCRUMB_KEY)).thenReturn(crumbs);
        when(context.sessionAttribute(Keys.ERROR_KEY)).thenReturn("error");
        when(context.sessionAttribute(Keys.INFO_KEY)).thenReturn("info");

        try (MockedStatic<DatabaseService> databaseService = mockStatic(DatabaseService.class)) {
            databaseService.when(() -> DatabaseService.activeDatabase(user.getUsername(), "test")).thenReturn(database);

            ViewHelper.defaultVariables(context);

            // then
            assertEquals("<a href='/database/test/'><ion-icon name=\"server-outline\"></ion-icon>test</a>", crumbs.get(0));
            verify(context, atLeastOnce()).sessionAttribute(Keys.DB_KEY, database);
            verify(context, atLeastOnce()).sessionAttribute("error", null);
            verify(context, atLeastOnce()).sessionAttribute("info", null);
        }
    }
}