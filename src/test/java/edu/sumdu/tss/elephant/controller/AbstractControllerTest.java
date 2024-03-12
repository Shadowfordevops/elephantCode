package edu.sumdu.tss.elephant.controller;

import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.utils.MessageBundle;
import edu.sumdu.tss.elephant.model.Database;
import edu.sumdu.tss.elephant.model.User;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractControllerTest {
    @Test
    void shouldGetCurrentUser() {
        // given
        Context context = mock(Context.class);
        User user = new User();
        user.setLogin("login");

        // when
        when(context.sessionAttribute(Keys.SESSION_CURRENT_USER_KEY)).thenReturn(user);

        // then
        assertEquals(user, AbstractController.currentUser(context));
    }

    @Test
    void shouldGetCurrentDB() {
        // given
        Context context = mock(Context.class);
        Database database = new Database();
        database.setName("database");

        // when
        when(context.sessionAttribute(Keys.DB_KEY)).thenReturn(database);

        // then
        assertEquals(database, AbstractController.currentDB(context));
    }

    @Test
    void shouldGetCurrentMessages() {
        // given
        Context context = mock(Context.class);
        MessageBundle mb = mock(MessageBundle.class);
        Map<String, Object> model = Map.of("msg", mb);

        // when
        when(context.sessionAttribute(Keys.MODEL_KEY)).thenReturn(model);

        // then
        assertEquals(mb, AbstractController.currentMessages(context));
    }

    @Test
    void shouldCreateMessageBundleWhenNoMessageBundle() {
        // given
        Context context = mock(Context.class);
        ResourceBundle rb = mock(ResourceBundle.class);

        // when
        try (MockedStatic<Keys> keys = mockStatic(Keys.class); MockedStatic<ResourceBundle> resourceBundle = mockStatic(ResourceBundle.class)) {
            keys.when(() -> Keys.get("DEFAULT_LANG")).thenReturn("EN");
            when(context.sessionAttribute(Keys.MODEL_KEY)).thenReturn(null);
            resourceBundle.when(() -> ResourceBundle.getBundle(any())).thenReturn(rb);

            // then
            assertNotNull(AbstractController.currentMessages(context));
        }
    }
}