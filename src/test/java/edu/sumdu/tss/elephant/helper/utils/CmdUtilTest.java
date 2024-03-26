package edu.sumdu.tss.elephant.helper.utils;

import edu.sumdu.tss.elephant.helper.exception.BackupException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CmdUtilTest {

    @Test
    void testValidCommandExec() {
        CmdUtil.exec("whoami");
    }

    @Test
    void testInvalidCommandExec() {
        assertThrows(BackupException.class, () -> {
            CmdUtil.exec("invalid command");
        });
    }
}
