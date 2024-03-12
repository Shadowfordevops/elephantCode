package edu.sumdu.tss.elephant.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {
    @Test
    void shouldSetAccordingValuesForAnyone() {
        // given
        UserRole userRole = UserRole.ANYONE;

        // when
        long maxConnections = userRole.maxConnections();
        long maxDB = userRole.maxDB();
        long maxStorage = userRole.maxStorage();
        long maxBackupsPerDB = userRole.maxBackupsPerDB();
        long maxScriptsPerDB = userRole.maxScriptsPerDB();

        // then
        assertEquals(0, maxConnections);
        assertEquals(0, maxDB);
        assertEquals(0, maxStorage);
        assertEquals(0, maxBackupsPerDB);
        assertEquals(0, maxScriptsPerDB);
    }
    @Test
    void shouldSetAccordingValuesForUnchecked() {
        // given
        UserRole userRole = UserRole.UNCHEKED;

        // when
        long maxConnections = userRole.maxConnections();
        long maxDB = userRole.maxDB();
        long maxStorage = userRole.maxStorage();
        long maxBackupsPerDB = userRole.maxBackupsPerDB();
        long maxScriptsPerDB = userRole.maxScriptsPerDB();

        // then
        assertEquals(0, maxConnections);
        assertEquals(0, maxDB);
        assertEquals(0, maxStorage);
        assertEquals(0, maxBackupsPerDB);
        assertEquals(0, maxScriptsPerDB);
    }

    @Test
    void shouldSetAccordingValuesForBasicUser() {
        // given
        UserRole userRole = UserRole.BASIC_USER;

        // when
        long maxConnections = userRole.maxConnections();
        long maxDB = userRole.maxDB();
        long maxStorage = userRole.maxStorage();
        long maxBackupsPerDB = userRole.maxBackupsPerDB();
        long maxScriptsPerDB = userRole.maxScriptsPerDB();

        // then
        assertEquals(5, maxConnections);
        assertEquals(2, maxDB);
        assertEquals(20_971_520L, maxStorage);
        assertEquals(1, maxBackupsPerDB);
        assertEquals(2, maxScriptsPerDB);
    }

    @Test
    void shouldSetAccordingValuesForPromotedUser() {
        // given
        UserRole userRole = UserRole.PROMOTED_USER;

        // when
        long maxConnections = userRole.maxConnections();
        long maxDB = userRole.maxDB();
        long maxStorage = userRole.maxStorage();
        long maxBackupsPerDB = userRole.maxBackupsPerDB();
        long maxScriptsPerDB = userRole.maxScriptsPerDB();

        // then
        assertEquals(5, maxConnections);
        assertEquals(3, maxDB);
        assertEquals(52_428_800L, maxStorage);
        assertEquals(5, maxBackupsPerDB);
        assertEquals(5, maxScriptsPerDB);
    }

    @Test
    void shouldSetAccordingValuesForAdmin() {
        // given
        UserRole userRole = UserRole.ADMIN;

        // when
        long maxConnections = userRole.maxConnections();
        long maxDB = userRole.maxDB();
        long maxStorage = userRole.maxStorage();
        long maxBackupsPerDB = userRole.maxBackupsPerDB();
        long maxScriptsPerDB = userRole.maxScriptsPerDB();

        // then
        assertEquals(5, maxConnections);
        assertEquals(100, maxDB);
        assertEquals(52_428_800L, maxStorage);
        assertEquals(10, maxBackupsPerDB);
        assertEquals(10, maxScriptsPerDB);
    }

    @Test
    void shouldGetRoleByValue() {
        // given
        int value = 3;

        // when
        UserRole userRole = UserRole.byValue(value);

        // then
        assertEquals(UserRole.PROMOTED_USER, userRole);
    }

    @Test
    void shouldNotGetRoleByValue_whenValueIsInvalid() {
        // given
        // when
        int value = 10;

        // then
        assertThrows(RuntimeException.class, () -> UserRole.byValue(value));
    }
}