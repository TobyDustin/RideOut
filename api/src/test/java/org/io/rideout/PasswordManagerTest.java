package org.io.rideout;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordManagerTest {

    @Test
    void testHashPasswordGeneratesCorrectly() {
        String password = PasswordManager.hashPassword("password");
        assertEquals("$2a$", password.substring(0, 4));
    }

    @Test
    void testVerifyValidHash() {
        String password = "password";
        String hash = "$2a$10$ueKeWIYp1w38bwJsg/GheOB2dl03Z.EvVZGvN7YLzm5l2DEzvmgsW";
        boolean valid = PasswordManager.verify(password, hash);

        assertTrue(valid);
    }

    @Test
    void testVerifyInvalidHash() {
        String password = "password";
        String hash = "$2a$10$ueKeWIYp1w3asd5dfg/GheOB2dl03Z.EvVZGvN7YLzm5l2DEzvmgsW";
        boolean valid = PasswordManager.verify(password, hash);

        assertFalse(valid);
    }
}
