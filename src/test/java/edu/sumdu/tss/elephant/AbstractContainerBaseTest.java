package edu.sumdu.tss.elephant;

import edu.sumdu.tss.elephant.helper.Keys;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.delegate.DatabaseDelegate;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.io.File;
import java.util.List;

public abstract class AbstractContainerBaseTest {
    public static final PostgreSQLContainer<?> DB_CONTAINER;

    static {
        Keys.loadParams(new File("src/test/resources/config.properties"));

        DB_CONTAINER = new PostgreSQLContainer<>("postgres:bullseye")
                .withUsername(Keys.get("DB.USERNAME"))
                .withPassword(Keys.get("DB.PASSWORD"))
                .withDatabaseName(Keys.get("DB.NAME"));

        DB_CONTAINER.setPortBindings(List.of(Keys.get("DB.PORT") + ":5432"));
        DB_CONTAINER.start();

        DatabaseDelegate delegate = new JdbcDatabaseDelegate(DB_CONTAINER, "");
        ScriptUtils.runInitScript(delegate, "migrations/20210801_create_basic_tables.sql");
        ScriptUtils.runInitScript(delegate, "migrations/20211105_add_description_to_scripts.sql");
        ScriptUtils.runInitScript(delegate, "migrations/20211220_alter_logger_ip_column.sql");
    }
}
