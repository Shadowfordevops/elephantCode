package edu.sumdu.tss.elephant.model;

import edu.sumdu.tss.elephant.helper.DBPool;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.helper.exception.NotFoundException;
import edu.sumdu.tss.elephant.helper.utils.ParameterizedStringFactory;
import org.junit.jupiter.api.*;
import org.sql2o.Connection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static edu.sumdu.tss.elephant.AbstractContainerBaseTest.DB_CONTAINER;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    private static final String INSERT_SQL =
            "insert into users(login, password, role, username, dbPassword, publickey, privatekey, token) " +
                    "values (:login, :password, :role, :username, :dbPassword, :publicKey, :privateKey, :token)";
    private static final ParameterizedStringFactory CREATE_USER_SQL = new ParameterizedStringFactory("CREATE USER :name WITH PASSWORD ':password' CONNECTION LIMIT 5 IN ROLE customer;");
    private static final String COUNT_SPACE = "SELECT count(*) FROM pg_catalog.pg_tablespace WHERE spcname = :username";

    private Connection connection;

    @BeforeAll
    static void beforeAll() {
        DB_CONTAINER.start();
        Keys.loadParams(new File("src/test/resources/config.properties"));
    }

    @BeforeEach
    void setUp() {
        connection = DBPool.getConnection().open();
    }

    @AfterEach
    void tearDown() {
        connection.close();
    }

    @Test
    void shouldFindUserByLogin() {
        // given
        String login = "test";

        connection.createQuery(INSERT_SQL)
                .addParameter("login", login)
                .addParameter("password", "test")
                .addParameter("role", UserRole.ANYONE.getValue())
                .addParameter("username", "test")
                .addParameter("dbPassword", "test")
                .addParameter("publicKey", "test")
                .addParameter("privateKey", "test")
                .addParameter("token", "test")
                .executeUpdate();

        // when
        User user = UserService.byLogin(login);

        // then
        assertEquals(login, user.getLogin());
    }

    @Test
    void shouldNotFindUserByLogin() {
        // given

        // when
        String login = "test_not_exist";

        // then
        assertThrows(NotFoundException.class, () -> UserService.byLogin(login));
    }

    @Test
    @Order(3)
    void shouldSaveUser() {
        // given
        User user = new User();
        user.setLogin("test1");
        user.setPassword("test1");
        user.setRole(UserRole.ANYONE.getValue());
        user.setUsername("test1");
        user.setDbPassword("test");
        user.setPublicKey("test1");
        user.setPrivateKey("test1");
        user.setToken("test1");

        // when
        UserService.save(user);

        // then
        int countUser = connection.createQuery("SELECT COUNT(*) FROM users WHERE login = :login")
                .addParameter("login", user.getLogin())
                .executeScalar(Integer.class);

        assertEquals(1, countUser);
    }

    @Test
    void shouldNotSaveDuplicateUser() {
        // given
        User user = new User();
        user.setLogin("test");
        user.setPassword("test");
        user.setRole(UserRole.ANYONE.getValue());
        user.setUsername("test");
        user.setDbPassword("test");
        user.setPublicKey("test");
        user.setPrivateKey("test");
        user.setToken("test");

        // when

        // then
        assertThrows(Exception.class, () -> UserService.save(user));
    }

    @Test
    void shouldCreateTablespace() throws IOException, InterruptedException {
        // given
        String username = "test_user_service";
        connection.createQuery(CREATE_USER_SQL.addParameter("name", username).addParameter("password", "test").toString(), false).executeUpdate();

        DB_CONTAINER.execInContainer("mkdir", "-p", "/home/" + username + "/tablespace");
        DB_CONTAINER.execInContainer("chown", "postgres:postgres", "/home/" + username + "/tablespace");

        // when
        UserService.createTablespace(username, "/home/test_user_service/tablespace");

        // then
        int countSpace = connection.createQuery(COUNT_SPACE).addParameter("username", username).executeScalar(Integer.class);
        assertEquals(1, countSpace);
    }

    @Test
    void shouldNotCreateTablespaceWhenNoUser() throws IOException, InterruptedException {
        // given
        String username = "test_not_exists";

        DB_CONTAINER.execInContainer("mkdir", "-p", "/home/" + username + "/tablespace");
        DB_CONTAINER.execInContainer("chown", "postgres:postgres", "/home/" + username + "/tablespace");

        // when
        // then
        assertThrows(Exception.class, () -> UserService.createTablespace(username, "/home/test_user_service/tablespace"));
    }

    @Test
    void shouldFindByPublicKey() {
        // given
        String publicKey = "test_user_service_1";

        connection.createQuery(INSERT_SQL)
                .addParameter("login", "test_user_service_1")
                .addParameter("password", "test")
                .addParameter("role", UserRole.ANYONE.getValue())
                .addParameter("username", "test_user_service_1")
                .addParameter("dbPassword", "test")
                .addParameter("publicKey", publicKey)
                .addParameter("privateKey", "test_user_service_1")
                .addParameter("token", "test")
                .executeUpdate();

        // when
        User user = UserService.byPublicKey(publicKey);

        // then
        assertEquals(publicKey, user.getPublicKey());
    }

    @Test
    @Order(1)
    void shouldInitUserStorage() {
        // given
        String username = "test_user_service_storage";

        // when
        UserService.initUserStorage(username);

        // then
        assertTrue(Files.exists(Paths.get(Keys.get("DB.LOCAL_PATH") + username + "/tablespace")));
    }

    @Test
    @Order(2)
    void shouldGetStorageSizeWhenExist() throws IOException {
        // given
        String username = "test_user_service_storage";

        // when
        Files.createFile(Paths.get(Keys.get("DB.LOCAL_PATH") + username + "/tablespace/test"));
        Files.write(Paths.get(Keys.get("DB.LOCAL_PATH") + username + "/tablespace/test"), "test".getBytes());
        long size = UserService.storageSize(username);

        // then
        assertTrue(size > 0);

        Files.deleteIfExists(Paths.get(Keys.get("DB.LOCAL_PATH") + username + "/tablespace/test"));
    }

    @Test
    void shouldNotGetStorageSizeWhenDoesntExist() {
        // given
        String username = "test_user_service_storage_doesnt_exist";

        // when
        long size = UserService.storageSize(username);

        // then
        assertEquals(0, size);
    }

    @Test
    void shouldGetDefaultUser() {
        // given
        // when
        User user = UserService.newDefaultUser();

        // then
        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals(UserRole.UNCHEKED.getValue(), user.getRole()),
                () -> assertEquals(User.USERNAME_SIZE, user.getUsername().length()),
                () -> assertEquals(User.DB_PASSWORD_SIZE, user.getDbPassword().length()),
                () -> assertEquals(User.API_KEY_SIZE, user.getPrivateKey().length()),
                () -> assertEquals(User.API_KEY_SIZE, user.getPublicKey().length()),
                () -> assertEquals(User.API_KEY_SIZE, user.getToken().length())
        );
    }

    @Test
    @Order(4)
    void shouldGetUserByToken() {
        // given
        User expected = new User();
        expected.setLogin("test1");
        expected.setPassword("test1");
        expected.setRole(UserRole.ANYONE.getValue());
        expected.setUsername("test1");
        expected.setDbPassword("test");
        expected.setPublicKey("test1");
        expected.setPrivateKey("test1");
        expected.setToken("test1");
        expected.setId(1L);
        expected.setLanguage("EN");

        // when
        User actual = UserService.byToken("test1");

        // then
        assertAll(
                () -> assertNotNull(expected),
                () -> assertEquals(expected, actual)
        );
    }


}