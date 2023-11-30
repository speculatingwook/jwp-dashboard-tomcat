package org.apache.coyote.http11.Session;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();

    public void add(Session session) {
        SESSIONS.put(session.getId(), session);
    }

    public Optional<Session> findSession(String id) {
        if (id != null) {
            return Optional.ofNullable(SESSIONS.get(id));
        } else {
            return Optional.empty();
        }
    }

    public void remove(Session session) {
        SESSIONS.remove(session.getId());
    }
}
