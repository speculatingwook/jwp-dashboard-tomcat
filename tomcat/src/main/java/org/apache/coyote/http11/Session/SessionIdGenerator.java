package org.apache.coyote.http11.Session;


import java.util.UUID;

public class SessionIdGenerator {

    private SessionIdGenerator() {
    }

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
