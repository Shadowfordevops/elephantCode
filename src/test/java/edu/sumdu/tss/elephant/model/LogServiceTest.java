package edu.sumdu.tss.elephant.model;

import edu.sumdu.tss.elephant.helper.DBPool;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import io.javalin.http.Context;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sql2o.Connection;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogServiceTest {

    private static class TestLog {
        String message;
    }

    @BeforeAll
    static void init() {
        String configFileName = "config.properties";
        File file = new File(configFileName);
        Keys.loadParams(file);
    }

    @Test
    @SneakyThrows
    void testPush() {
        User user = UserService.newDefaultUser();
        user.setLogin(StringUtils.randomAlphaString(8) + "@example.com");
        user.setPassword(StringUtils.randomAlphaString(8));
        String databaseName = StringUtils.randomAlphaString(8);

        Context context = Mockito.mock(Context.class);
        Mockito.when(context.ip()).thenReturn("127.0.0.1");
        Mockito.when(context.sessionAttribute(Keys.SESSION_CURRENT_USER_KEY)).thenReturn(user);

        String message = StringUtils.randomAlphaString(8);
        LogService.push(context, databaseName, message);

        TestLog result;
        try (Connection con = DBPool.getConnection().open()) {;
            result = con.createQuery("SELECT message FROM LOGGER WHERE \"user\" = :user AND database = :database")
                    .addParameter("user", user.getLogin())
                    .addParameter("database", databaseName)
                    .executeAndFetchFirst(TestLog.class);
        }
        assertEquals(message, result.message);
    }
}
