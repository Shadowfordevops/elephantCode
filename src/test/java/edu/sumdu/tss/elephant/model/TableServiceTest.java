package edu.sumdu.tss.elephant.model;

import edu.sumdu.tss.elephant.helper.DBPool;
import edu.sumdu.tss.elephant.helper.Keys;
import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.data.Table;

import java.io.File;

import static edu.sumdu.tss.elephant.AbstractContainerBaseTest.DB_CONTAINER;
import static org.junit.jupiter.api.Assertions.*;

class TableServiceTest {
    private Connection connection;

    @BeforeAll
    public static void init() {
        Keys.loadParams(new File("src/test/resources/config.properties"));
        DB_CONTAINER.start();

        prepareData();
    }

    @BeforeEach
    public void setUp() {
        connection = DBPool.getConnection().open();
    }

    @AfterEach
    public void tearDown() {
        connection.close();
    }

    @AfterAll
    public static void destroy() {
        Connection connection = DBPool.getConnection().open();
        connection.createQuery("DROP TABLE IF EXISTS test;").executeUpdate();
        connection.close();
    }

    static void prepareData() {
        Connection connection = DBPool.getConnection().open();
        connection.createQuery("CREATE TABLE test(id int);").executeUpdate();
        connection.createQuery("INSERT INTO test(id) VALUES(1);").executeUpdate();
        connection.createQuery("INSERT INTO test(id) VALUES(2);").executeUpdate();
        connection.createQuery("INSERT INTO test(id) VALUES(3);").executeUpdate();
        connection.close();
    }

    @Test
    void shouldList() {
        // given
        String database = "test";

        // when
        Table table = TableService.list(database);

        // then
        assertAll(
                () -> assertFalse(table.rows().isEmpty()),
                () -> assertEquals("tables", table.getName()),
                () -> assertTrue(table.rows().stream().anyMatch(row -> row.getString("name").equals("test")))
        );
    }

    @Test
    void shouldGetTableSize() {
        // given
        String database = "test";
        String table = "test";

        // when
        int size = TableService.getTableSize(database, table);

        // then
        assertEquals(3, size);
    }

    @Test
    void shouldNotGetTableSize() {
        // given
        String database = "test";
        String table = "test2";

        // when
        int size = TableService.getTableSize(database, table);

        // then
        assertEquals(0, size);
    }

    @Test
    void shouldGetTableByName() {
        // given
        String database = "test";
        String table = "test";
        int limit = 2;
        int offset = 0;

        // when
        Table result = TableService.byName(database, table, limit, offset);

        // then
        assertAll(
                () -> assertEquals("test", result.getName()),
                () -> assertEquals(2, result.rows().size())
        );
    }

    @Test
    void shouldGetAllDataFromTableByName() {
        // given
        String database = "test";
        String table = "test";
        int limit = 10;
        int offset = 0;

        // when
        Table result = TableService.byName(database, table, limit, offset);

        // then
        assertAll(
                () -> assertEquals("test", result.getName()),
                () -> assertEquals(3, result.rows().size())
        );
    }

    @Test
    void shouldNotGetTableByName() {
        // given
        String database = "test";
        String table = "test2";
        int limit = 1;
        int offset = 0;

        // when

        // then
        assertThrows(Exception.class, () ->
                TableService.byName(database, table, limit, offset));
    }
}