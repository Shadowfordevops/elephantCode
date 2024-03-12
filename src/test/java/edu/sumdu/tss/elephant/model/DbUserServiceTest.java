package edu.sumdu.tss.elephant.model;

import edu.sumdu.tss.elephant.helper.DBPool;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.utils.ParameterizedStringFactory;
import org.junit.jupiter.api.*;
import org.sql2o.Connection;

import java.io.File;
import java.io.IOException;

import static edu.sumdu.tss.elephant.AbstractContainerBaseTest.DB_CONTAINER;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DbUserServiceTest {
    private static final String COUNT_USER = "SELECT count(*) FROM pg_catalog.pg_user WHERE usename = :username";
    private static final String COUNT_SPACE = "SELECT count(*) FROM pg_catalog.pg_tablespace WHERE spcname = :username";
    private static final ParameterizedStringFactory CREATE_USER_SQL = new ParameterizedStringFactory("CREATE USER :name WITH PASSWORD ':password' CONNECTION LIMIT 5 IN ROLE customer;");

    private Connection connection;

    @BeforeAll
    public static void init() {
        DB_CONTAINER.start();
        Keys.loadParams(new File("src/test/resources/config.properties"));
    }

    @BeforeEach
    public void setUp() {
        connection = DBPool.getConnection().open();
    }

    @AfterEach
    public void tearDown() {
        connection.close();
    }

    @Test
    @Order(1)
    void shouldNotInitUserWhenNoDirectory() {
        // given
        String username = "test_no_directory";
        String password = "test";

        // when

        // then
        assertThrows(Exception.class, () ->
                DbUserService.initUser(username, password)
        );
    }

    @Test
    @Order(2)
    void shouldInitUser() throws IOException, InterruptedException {
        // given
        String username = "test";
        String password = "test";

        // when
        DB_CONTAINER.execInContainer("mkdir", "-p", "/home/" + username + "/tablespace");
        DB_CONTAINER.execInContainer("chown", "postgres:postgres", "/home/" + username + "/tablespace");
        DbUserService.initUser(username, password);

        // then
        int countUser = connection.createQuery(COUNT_USER).addParameter("username", username).executeScalar(Integer.class);
        int countSpace = connection.createQuery(COUNT_SPACE).addParameter("username", username).executeScalar(Integer.class);

        assertAll(
                () -> assertEquals(1, countUser),
                () -> assertEquals(1, countSpace)
        );
    }

    @Test
    @Order(3)
    void shouldResetUserPassword() {
        // given
        String username = "test";
        String password = "test123";

        // when
        DbUserService.dbUserPasswordReset(username, password);

        // then
        int countUser = connection.createQuery(COUNT_USER).addParameter("username", username).executeScalar(Integer.class);

        assertEquals(1, countUser);
    }

    @Test
    @Order(4)
    void shouldNotDropUserWhenItHasSpace() {
        // given
        String username = "test";

        // when

        // then
        assertThrows(Exception.class, () ->
                DbUserService.dropUser(username)
        );
    }

    @Test
    @Order(5)
    void shouldDropUser() {
        // given
        String username = "test_drop";
        String password = "test";
        connection.createQuery(CREATE_USER_SQL.addParameter("name", username).addParameter("password", password).toString(), false).executeUpdate();

        // when
        DbUserService.dropUser(username);

        // then
        int countUser = connection.createQuery(COUNT_USER).addParameter("username", username).executeScalar(Integer.class);
        int countSpace = connection.createQuery(COUNT_SPACE).addParameter("username", username).executeScalar(Integer.class);

        assertAll(
                () -> assertEquals(1, countUser),
                () -> assertEquals(1, countSpace)
        );
    }

    @AfterAll
    public static void destroy() {
        Connection connection = DBPool.getConnection().open();
        connection.createQuery("drop tablespace test").executeUpdate();
        connection.createQuery("drop user test").executeUpdate();
        connection.close();
    }
}