package edu.sumdu.tss.elephant.model;

import edu.sumdu.tss.elephant.helper.DBPool;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.exception.NotFoundException;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BackupServiceTests {

    private static final String DATABASE = "elephant";
    private static final String BACKUP_NAME = "backupName";

    private static User user;

    @BeforeAll
    static void init() {
        String configFileName = "config.properties";
        File file = new File(configFileName);
        Keys.loadParams(file);

        user = UserService.newDefaultUser();
        user.setLogin(StringUtils.randomAlphaString(8) + "@test.com");
        user.setPassword(StringUtils.randomAlphaString(8));
        user.setLanguage("EN");
        UserService.save(user);
        try {
            DatabaseService.byName("elephant");
        } catch (Exception ignored) {
            DBPool.getConnection().createQuery("insert into databases(name, owner) values(:name, :owner)")
                    .addParameter("name", "elephant")
                    .addParameter("owner", user.getUsername())
                    .executeUpdate();
        }
    }

    @Test
    void testList() {
        var result = BackupService.list(DATABASE);
        assertNotNull(result);
    }

    @Test
    @Order(2)
    void testPerform() {
        assertThrows(Exception.class, () -> BackupService.perform(user.getUsername(), DATABASE, BACKUP_NAME));
    }

    @Test
    @Order(3)
    void testListWithSome() {
        var result = BackupService.list(DATABASE);
        assertNotNull(result);
    }

    @Test
    @Order(4)
    void testRestoreBackup() {
        assertThrows(Exception.class, () -> BackupService.restore(user.getUsername(), DATABASE, BACKUP_NAME));
    }
}
