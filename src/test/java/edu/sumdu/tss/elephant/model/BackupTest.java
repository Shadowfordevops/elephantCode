package edu.sumdu.tss.elephant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BackupTest {
    @Test
    void shouldGetBackupStateByValue() {
        assertEquals(Backup.BackupState.PERFORMED, Backup.BackupState.valueOf("PERFORMED"));
    }
}