package edu.sumdu.tss.elephant.helper;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.sql2o.Sql2o;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class DBPoolTest {
    @Test
    void shouldGetConnection() {
        // given
        try (MockedStatic<Keys> keys = mockStatic(Keys.class)) {
            keys.when(() -> Keys.get("DB.USERNAME")).thenReturn("username");
            keys.when(() -> Keys.get("DB.PASSWORD")).thenReturn("password");
            keys.when(() -> Keys.get("DB.URL")).thenReturn("url");
            keys.when(() -> Keys.get("DB.PORT")).thenReturn("port");

            // when
            Sql2o connection = DBPool.getConnection("dbname");

            // then
            assertNotNull(connection);
            assertInstanceOf(Sql2o.class, connection);
        }
    }

    @Test
    void shouldGetDefaultConnection() {
        // given
        try (MockedStatic<Keys> keys = mockStatic(Keys.class)) {
            keys.when(() -> Keys.get("DB.NAME")).thenReturn("dbname");
            keys.when(() -> Keys.get("DB.USERNAME")).thenReturn("username");
            keys.when(() -> Keys.get("DB.PASSWORD")).thenReturn("password");
            keys.when(() -> Keys.get("DB.URL")).thenReturn("url");
            keys.when(() -> Keys.get("DB.PORT")).thenReturn("port");

            // when
            Sql2o connection = DBPool.getConnection();

            // then
            assertNotNull(connection);
            assertInstanceOf(Sql2o.class, connection);
        }
    }
    @Test
    void testDbUtilUrl() {
        // given
        try (MockedStatic<Keys> keys = mockStatic(Keys.class)) {
            keys.when(() -> Keys.get("DB.USERNAME")).thenReturn("username");
            keys.when(() -> Keys.get("DB.PASSWORD")).thenReturn("password");
            keys.when(() -> Keys.get("DB.URL")).thenReturn("url");
            keys.when(() -> Keys.get("DB.PORT")).thenReturn("port");

            // when
            String url = DBPool.dbUtilUrl("test");

            // then
            assertEquals("postgresql://username:password@url:port/test", url);
        }
    }

    @Test
    void shouldFlush_whenExceedLimit() {
        // given
        try (MockedStatic<Keys> keys = mockStatic(Keys.class)) {
            for (int i = 0; i < 10; i++) {
                keys.when(() -> Keys.get("DB.USERNAME")).thenReturn("username");
                keys.when(() -> Keys.get("DB.PASSWORD")).thenReturn("password");
                keys.when(() -> Keys.get("DB.URL")).thenReturn("url");
                keys.when(() -> Keys.get("DB.PORT")).thenReturn("port");

                Sql2o connection = DBPool.getConnection("dbname" + i);
                assertNotNull(connection);
            }

            // when exceeds connection limit
            keys.when(() -> Keys.get("DB.USERNAME")).thenReturn("username");
            keys.when(() -> Keys.get("DB.PASSWORD")).thenReturn("password");
            keys.when(() -> Keys.get("DB.URL")).thenReturn("url");
            keys.when(() -> Keys.get("DB.PORT")).thenReturn("port");

            Sql2o connection = DBPool.getConnection("dbname10");

            // then
            assertNotNull(connection);
            assertInstanceOf(Sql2o.class, connection);
        }
    }
}