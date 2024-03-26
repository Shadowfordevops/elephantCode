package edu.sumdu.tss.elephant.model;

import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TableServiceTests {

    @BeforeAll
    static void init() {
        String configFileName = "config.properties";
        File file = new File(configFileName);
        Keys.loadParams(file);
    }

    @Test
    void testList() {
        String database = "elephant";
        var result = TableService.list(database);
        assertNotNull(result);
    }

    @Test
    void testTableSize() {
        String database = "elephant";
        String table = "users";

        User user = UserService.newDefaultUser();
        user.setLogin(StringUtils.randomAlphaString(8) + "@test.com");
        user.setPassword(StringUtils.randomAlphaString(8));
        UserService.save(user);

        var result = TableService.getTableSize(database, table);
        assertTrue(result > 0);
    }

    @Test
    void testTableSizeZero() {
        String database = "elephant";
        String table = "incorrect";
        var result = TableService.getTableSize(database, table);
        assertEquals(result, 0);
    }

    @Test
    void testByName() {
        String database = "elephant";
        String tableName = "users";

        User user = UserService.newDefaultUser();
        user.setLogin(StringUtils.randomAlphaString(8) + "@test.com");
        user.setPassword(StringUtils.randomAlphaString(8));
        UserService.save(user);

        int limit = 10;
        int offset = 0;
        var result = TableService.byName(database, tableName, limit, offset);
        assertNotNull(result);
    }
}
