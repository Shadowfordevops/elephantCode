package edu.sumdu.tss.elephant.helper.utils.enums;

import edu.sumdu.tss.elephant.helper.enums.Lang;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LangTest {

    @Test
    void testByValueEN() {
        Lang actual = Lang.byValue("EN");
        assertEquals(actual, Lang.EN);
    }

    @Test
    void testByValueUK() {
        Lang actual = Lang.byValue("UK");
        assertEquals(actual, Lang.UK);
    }

    @Test
    void testByValueInvalidParameter() {
        assertThrows(RuntimeException.class, () -> {
            Lang.byValue("invalid parameter");
        });
    }
}
