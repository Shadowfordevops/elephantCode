package edu.sumdu.tss.elephant.model;

import edu.sumdu.tss.elephant.helper.DBPool;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.helper.exception.NotFoundException;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import lombok.SneakyThrows;
import org.apache.maven.surefire.shared.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private final String ADD_USER = "INSERT INTO USERS(login, password, role, username, dbPassword, publickey, privatekey, token) " +
            "VALUES (:login, :password, :role, :username, :dbPassword, :publicKey, :privateKey, :token)";

    @BeforeAll
    static void init() {
        String configFileName = "config.properties";
        File file = new File(configFileName);
        Keys.loadParams(file);
    }

    @Test
    void testByLogin() {
        User user = UserService.newDefaultUser();
        user.setLogin(StringUtils.randomAlphaString(8) + "@test.com");
        user.setPassword(StringUtils.randomAlphaString(8));

        try (Connection con = DBPool.getConnection().open()) {
            Long id = con.createQuery(ADD_USER).bind(user).executeUpdate().getKey(Long.class);
            user.setId(id);
            user.setLanguage("EN");
        }

        User result = UserService.byLogin(user.getLogin());
        assertEquals(user, result);
    }

    @Test
    void testByLoginException() {
        assertThrows(NotFoundException.class, () -> {
                    UserService.byLogin(StringUtils.randomAlphaString(10));
                }
        );
    }

    @Test
    void testSave() {
        User user = UserService.newDefaultUser();
        user.setLogin(StringUtils.randomAlphaString(8) + "@test.com");
        user.setPassword(StringUtils.randomAlphaString(8));
        user.setLanguage("EN");

        UserService.save(user);

        User result = UserService.byLogin(user.getLogin());
        assertEquals(result, user);
    }

    @Test
    void testSaveUpdate() {
        User user = UserService.newDefaultUser();
        user.setLogin(StringUtils.randomAlphaString(8) + "@test.com");
        user.setPassword(StringUtils.randomAlphaString(8));

        UserService.save(user);
        user = UserService.byLogin(user.getLogin());
        user.setUsername(StringUtils.randomAlphaString(8) + "@test.com");
        UserService.save(user);

        User result = UserService.byLogin(user.getLogin());
        assertEquals(result.getUsername(), user.getUsername());
    }

    @Test
    void testSaveException() {
        User user = UserService.newDefaultUser();
        user.setLogin(StringUtils.randomAlphaString(8) + "@test.com");
        user.setPassword(StringUtils.randomAlphaString(8));
        user.setLanguage("EN");
        user.setId(9999999L);

        assertThrows(RuntimeException.class, () -> {
            UserService.save(user);
        });
    }

    @Test
    void testByPublicKey() {
        User user = UserService.newDefaultUser();
        user.setLogin(StringUtils.randomAlphaString(8) + "@test.com");
        user.setPassword(StringUtils.randomAlphaString(8));

        try (Connection con = DBPool.getConnection().open()) {
            Long id = con.createQuery(ADD_USER).bind(user).executeUpdate().getKey(Long.class);
            user.setId(id);
            user.setLanguage("EN");
        }

        User result = UserService.byPublicKey(user.getPublicKey());
        assertEquals(user, result);
    }

    @Test
    void testUserStoragePath() {
        String username = StringUtils.randomAlphaString(8);
        String userpath = Keys.get("DB.LOCAL_PATH") + username;
        assertEquals(userpath, UserService.userStoragePath(username));
    }

    @Test
    void testInitUserStorage() {
        String username = StringUtils.randomAlphaString(8);
        String userpath = Keys.get("DB.LOCAL_PATH") + username;

        UserService.initUserStorage(username);
        assertTrue(FileUtils.isDirectory(new File(userpath + File.separator + "tablespace")));
        assertTrue(FileUtils.isDirectory(new File(userpath + File.separator + "scripts")));
        assertTrue(FileUtils.isDirectory(new File(userpath + File.separator + "backups")));
    }

    @Test
    @SneakyThrows
    void testStorageSize() {
        String username = StringUtils.randomAlphaString(8);
        String userpath = Keys.get("DB.LOCAL_PATH") + username;
        UserService.initUserStorage(username);

        File dummyFile = new File(userpath + "/dummyfile.txt");
        byte[] dummyData = new byte[30 * 1024];
        FileUtils.writeByteArrayToFile(dummyFile, dummyData);

        assertEquals(FileUtils.sizeOfDirectory(new File(userpath)), UserService.storageSize(username));
    }

    @Test
    void testStorageSizeException() {
        String username = StringUtils.randomAlphaString(8);
        assertEquals(0, UserService.storageSize(username));
    }

    @Test
    void testNewDefaultUser() {
        User user = UserService.newDefaultUser();
        assertNotNull(user);
        assertNull(user.getLogin());
        assertNull(user.getPassword());
        assertNull(user.getId());
        assertEquals(user.getRole(), UserRole.UNCHEKED.getValue());
        assertTrue(user.getUsername() != null && user.getUsername().length() == User.USERNAME_SIZE);
        assertTrue(user.getDbPassword() != null && user.getDbPassword().length() == User.DB_PASSWORD_SIZE);
        assertTrue(user.getPrivateKey() != null && user.getPrivateKey().length() == User.API_KEY_SIZE);
        assertTrue(user.getPublicKey() != null && user.getPublicKey().length() == User.API_KEY_SIZE);
        assertTrue(user.getToken() != null && user.getToken().length() == User.API_KEY_SIZE);
    }

    @Test
    void testByToken() {
        User user = UserService.newDefaultUser();
        user.setLogin(StringUtils.randomAlphaString(8) + "@test.com");
        user.setPassword(StringUtils.randomAlphaString(8));

        try (Connection con = DBPool.getConnection().open()) {
            Long id = con.createQuery(ADD_USER).bind(user).executeUpdate().getKey(Long.class);
            user.setId(id);
            user.setLanguage("EN");
        }

        User result = UserService.byToken(user.getToken());
        assertEquals(user, result);
    }

    @Test
    void testByTokenException() {
        assertThrows(NotFoundException.class, () -> {
                    UserService.byToken(StringUtils.randomAlphaString(5));
                }
        );
    }
}
