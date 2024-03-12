package edu.sumdu.tss.elephant.controller;

import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.model.Database;
import edu.sumdu.tss.elephant.model.DatabaseService;
import edu.sumdu.tss.elephant.model.User;
import edu.sumdu.tss.elephant.model.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {
    @Mock private Javalin app;
    @InjectMocks private HomeController homeController;

    @Test
    void shouldRegister() {
        // when
        homeController.register(app);

        // then
        verify(app, atLeastOnce()).get(eq("/"), any(), any());
        verify(app, atLeastOnce()).get(eq(HomeController.BASIC_PAGE), any(), any());
    }

    @Test
    void shouldShow() {
        // given
        Context context = mock(Context.class);

        User user = new User();
        user.setUsername("test");
        user.setRole(UserRole.BASIC_USER.getValue());

        Map<String, Object> model = new HashMap<>();
        List<Database> databases = new ArrayList<>();

        // when
        when(context.sessionAttribute(Keys.SESSION_CURRENT_USER_KEY)).thenReturn(user);
        when(context.sessionAttribute(Keys.MODEL_KEY)).thenReturn(model);

        try (MockedStatic<DatabaseService> databaseService = mockStatic(DatabaseService.class);
             MockedStatic<UserService> userService = mockStatic(UserService.class);
             MockedStatic<Keys> keys = mockStatic(Keys.class)) {
            databaseService.when(() -> DatabaseService.forUser(user.getUsername())).thenReturn(databases);
            userService.when(() -> UserService.storageSize(user.getUsername())).thenReturn(13_631_466L);
            keys.when(() -> Keys.get("DB.HOST")).thenReturn("test host");
            keys.when(() -> Keys.get("DB.PORT")).thenReturn("test port");

            HomeController.show(context);

            // then
            verify(context, atLeastOnce()).render(eq("/velocity/home/show.vm"), any());
            assertAll(
                    () -> assertEquals(13_631_466L, model.get("sizeUsed")),
                    () -> assertEquals(20971520L, model.get("sizeTotal")),
                    () -> assertEquals(65L, model.get("spacePercent")),
                    () -> assertEquals(databases, model.get("bases")),
                    () -> assertEquals("test host", model.get("host")),
                    () -> assertEquals("test port", model.get("port"))
            );
        }
    }
}