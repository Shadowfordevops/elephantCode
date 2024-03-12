package edu.sumdu.tss.elephant.helper;

import com.google.common.collect.Streams;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KeysTest {
    static private Path props;

    @BeforeAll
    static void setUp() throws IOException {
        props = Files.createTempFile("test", ".properties");
        Files.write(props, new ArrayList<>());
    }

    private List<String> getBasicProperties() {
        return Arrays.stream(Keys.PARAMS).map(key -> key + "=test").toList();
    }

    private List<String> getSecuredProperties() {
        return Arrays.stream(Keys.SECURED_PARAMS).map(key -> key + "=test").toList();
    }

    @Test
    @Order(0)
    void shouldNotLoadDatabaseParamWhenKeysAreNotSet() {
        assertThrows(RuntimeException.class, () -> Keys.get(Keys.DB_KEY));
    }

    @Test
    @Order(1)
    void shouldNotLoadParamsWhenFileNotExists() {
        assertThrows(RuntimeException.class, () -> Keys.loadParams(new File("nonexistent.properties")));
    }

    @Test
    @Order(2)
    void shouldNotLoadParamsWhenFileIsEmpty() throws IOException {
        Files.write(props, new ArrayList<>());
        assertThrows(RuntimeException.class, () -> Keys.loadParams(props.toFile()));
    }

    @Test
    @Order(3)
    void shouldNotLoadParamsWhenNoSecuredParams() throws IOException {
        Files.write(props, getBasicProperties());
        assertThrows(RuntimeException.class, () -> Keys.loadParams(props.toFile()));
    }

    @Test
    @Order(4)
    void shouldLoadParams() throws IOException {
        Files.write(props, Streams.concat(getBasicProperties().stream(), getSecuredProperties().stream()).toList());
        assertDoesNotThrow(() -> Keys.loadParams(props.toFile()));
    }

    @Test
    @Order(5)
    void shouldGetParam() {
        assertEquals("test", Keys.get(Keys.PARAMS[0]));
    }

    @Test
    @Order(6)
    void shouldNotGetUnknownParam() {
        assertThrows(RuntimeException.class, () -> Keys.get("unknown"));
    }

    @Test
    @Order(7)
    void shouldThrowExceptionIfParamIsEmpty() throws IOException {
        Files.write(props, Streams.concat(getBasicProperties().stream(), getSecuredProperties().stream(), Stream.of("empty=")).toList());
        assertDoesNotThrow(() -> Keys.loadParams(props.toFile()));
        assertThrows(RuntimeException.class, () -> Keys.get("empty"));
    }

    @Test
    @Order(8)
    void shouldThrowExceptionIfParamIsNotSet() {
        assertThrows(RuntimeException.class, () -> Keys.get("empty"));
    }

    @Test
    @Order(9)
    void shouldNotBeProduction() {
        assertFalse(Keys.isProduction());
    }

    @Test
    @Order(10)
    void shouldBeProduction() throws IOException {
        Files.write(props, Streams.concat(getBasicProperties().stream(), getSecuredProperties().stream(), Stream.of("empty=test", "ENV=production")).toList());
        assertDoesNotThrow(() -> Keys.loadParams(props.toFile()));
        assertTrue(Keys.isProduction());
    }
}