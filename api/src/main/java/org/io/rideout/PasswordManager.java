package org.io.rideout;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {
    private PasswordManager() {}

    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    public static boolean verify(String password, String hash) {
        if (hash == null || !hash.startsWith("$2a$")) {
            throw new IllegalArgumentException("Invalid hash");
        }

        return BCrypt.checkpw(password, hash);
    }
}
