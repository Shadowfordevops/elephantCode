package edu.sumdu.tss.elephant.middleware;

import edu.sumdu.tss.elephant.controller.AbstractController;
import edu.sumdu.tss.elephant.controller.HomeController;
import edu.sumdu.tss.elephant.controller.LoginController;
import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.model.User;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAccessManagerTest {
    @Mock
    private Context ctx;
    @Mock
    private Set<RouteRole> permittedRoles;
    @Mock
    private Handler handler;

    @Test
    void shouldGetUserRole() throws Exception {
        // given
        User user = mock(User.class);

        // when
        when(permittedRoles.size()).thenReturn(1);
        when(user.role()).thenReturn(UserRole.ANYONE);
        when(permittedRoles.contains(UserRole.ANYONE)).thenReturn(true);

        try (MockedStatic<AbstractController> mocked = mockStatic(AbstractController.class)) {
            mocked.when(() -> AbstractController.currentUser(ctx)).thenReturn(user);

            CustomAccessManager.accessManager.manage(handler, ctx, permittedRoles);

            // then
            verify(handler).handle(ctx);
        }
    }

    @Test
    void shouldWorkWhenNoRoles() throws Exception {
        // given
        User user = mock(User.class);

        // when
        when(permittedRoles.size()).thenReturn(0);

        try (MockedStatic<AbstractController> mocked = mockStatic(AbstractController.class)) {
            mocked.when(() -> AbstractController.currentUser(ctx)).thenReturn(user);

            CustomAccessManager.accessManager.manage(handler, ctx, permittedRoles);

            // then
            verify(handler).handle(ctx);
        }
    }

    @Test
    void shouldWorkWhenNoUser() throws Exception {
        // given

        // when
        when(permittedRoles.size()).thenReturn(1);
        when(permittedRoles.contains(UserRole.ANYONE)).thenReturn(true);

        try (MockedStatic<AbstractController> mocked = mockStatic(AbstractController.class)) {
            mocked.when(() -> AbstractController.currentUser(ctx)).thenReturn(null);

            CustomAccessManager.accessManager.manage(handler, ctx, permittedRoles);

            // then
            verify(handler).handle(ctx);
        }
    }

    @Test
    void shouldRedirectToLoginPage() throws Exception {
        // given
        User user = mock(User.class);

        // when
        when(permittedRoles.size()).thenReturn(1);
        when(user.role()).thenReturn(UserRole.ANYONE);
        when(permittedRoles.contains(UserRole.ANYONE)).thenReturn(false);

        try (MockedStatic<AbstractController> mocked = mockStatic(AbstractController.class)) {
            mocked.when(() -> AbstractController.currentUser(ctx)).thenReturn(user);

            CustomAccessManager.accessManager.manage(handler, ctx, permittedRoles);

            // then
            verify(ctx).redirect(LoginController.BASIC_PAGE, 302);
        }
    }

    @Test
    void shouldRedirectToHomePage() throws Exception {
        // given
        User user = mock(User.class);

        // when
        when(permittedRoles.size()).thenReturn(1);
        when(user.role()).thenReturn(UserRole.BASIC_USER);
        when(permittedRoles.contains(UserRole.BASIC_USER)).thenReturn(false);

        try (MockedStatic<AbstractController> mocked = mockStatic(AbstractController.class)) {
            mocked.when(() -> AbstractController.currentUser(ctx)).thenReturn(user);

            CustomAccessManager.accessManager.manage(handler, ctx, permittedRoles);

            // then
            verify(ctx).redirect(HomeController.BASIC_PAGE, 302);
        }
    }
}