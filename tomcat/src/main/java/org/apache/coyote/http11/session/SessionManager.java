package org.apache.coyote.http11.session;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    public static String generateJSessionId() {
        return UUID.randomUUID().toString();
    }

    private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();

    public static void add(Session session) {
        SESSIONS.put(session.getSessionId(), session);
    }

    public static Optional<Session> findSession(String sessionId) {
        return Optional.ofNullable(SESSIONS.get(sessionId));
    }

    public void remove(Session session) {
        SESSIONS.remove(session.getSessionId());
    }
}
