package org.example.until;

import org.hashids.Hashids;

public class UniqueIDGenerator {
    private static final String SALT = "my-salt-string";
    private static final int MIN_LENGTH = 8;

    public static String generateUniqueID() {
        Hashids hashids = new Hashids(SALT, MIN_LENGTH);
        long now = System.currentTimeMillis();
        return hashids.encode(now);
    }
}