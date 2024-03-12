package edu.sumdu.tss.elephant.model;

import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.helper.exception.HttpError500;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class UserTest {
    @Test
    public void shouldCrypt() {
        // given
        User user = new User();
        user.setLogin("login");

        // when
        String source = "password";
        String expectedHash = "3946f08d19b35accd9379d7fdf28a5c29ad78a81928ffc49671de3191a5ca5abaf434f0f5eac8c326c2ac473d9fe7e7f";

        String actualHash = user.crypt(source);

        // then
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void shouldCryptPassword() {
        // given
        User user = new User();
        user.setLogin("login");

        // when
        String source = "password";
        String expectedHash = "3946f08d19b35accd9379d7fdf28a5c29ad78a81928ffc49671de3191a5ca5abaf434f0f5eac8c326c2ac473d9fe7e7f";

        user.password(source);

        // then
        assertEquals(expectedHash, user.getPassword());
    }

    @Test
    public void shouldNotCryptWhenNoAlgorithm() {
        // given
        String source = "password";

        // when
        try (MockedStatic<MessageDigest> messageDigest = mockStatic(MessageDigest.class)) {
            messageDigest.when(() -> MessageDigest.getInstance("SHA-384")).thenThrow(NoSuchAlgorithmException.class);

            // then
            assertThrows(HttpError500.class, () -> {
                User user = new User();
                user.setLogin("login");
                user.password(source);
                user.crypt(source);
            });
        }
    }

    @Test
    public void shouldReturnRole() {
        // given
        User user = new User();
        user.setRole(0L);

        // when
        UserRole expectedRole = UserRole.ANYONE;

        // then
        assertEquals(expectedRole, user.role());
    }

    @Test
    public void shouldGenerateNewToken_whenResetToken() {
        // given
        User user = new User();

        // when
        user.resetToken();
        String oldToken = user.getToken();
        user.resetToken();

        // then
        assertNotEquals(oldToken, user.getToken());
    }
}
