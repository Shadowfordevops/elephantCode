package edu.sumdu.tss.elephant.model;

import edu.sumdu.tss.elephant.helper.DBPool;
import edu.sumdu.tss.elephant.helper.Keys;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sql2o.Connection;

import static org.mockito.Mockito.*;

class LogServiceTest {
    @Test
    void shouldPush() {
        // given
        try (MockedStatic<Keys> keys = mockStatic(Keys.class)) {

            keys.when(() -> Keys.get(any())).thenReturn("key");
            Sql2o sql2o = mock(Sql2o.class);
            Connection connection = mock(Connection.class);
            Context context = mock(Context.class);
            Query query = mock(Query.class);
            User user = mock(User.class);

            try (MockedStatic<DBPool> dbPool = mockStatic(DBPool.class)) {
                // when
                dbPool.when(DBPool::getConnection).thenReturn(sql2o);
                when(sql2o.open()).thenReturn(connection);
                when(connection.createQuery(anyString())).thenReturn(query);
                when(context.sessionAttribute(Keys.SESSION_CURRENT_USER_KEY)).thenReturn(user);
                when(context.ip()).thenReturn("ip");
                when(user.getLogin()).thenReturn("login");
                when(query.addParameter(anyString(), anyString())).thenReturn(query);

                LogService.push(context, "database", "message");

                // then
                verify(query).addParameter("database", "database");
                verify(query).addParameter("message", "message");
                verify(query).addParameter("user", "login");
                verify(query).addParameter("ip", "ip");
                verify(query).executeUpdate();
            }
        }
    }
}