package edu.sumdu.tss.elephant.helper;

import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class HmacTest {

    @Test
    void shouldCalculate() throws NoSuchAlgorithmException, InvalidKeyException {
        // given
        String data = "data";
        String key = "key";

        // when
        String result = Hmac.calculate(data, key);

        // then
        assertEquals("c5f97ad9fd1020c174d7dc02cf83c4c1bf15ee20ec555b690ad58e62da8a00ee44ccdb65cb8c80acfd127ebee568958a", result);
    }
}