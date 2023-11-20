package org.apache.coyote.http11.session;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private static Map<String, Session> sessions = new HashMap<>();
    private String sessionId;
    private Map<String, String> attributes;

    private Session(String sessionId) {
        this.sessionId = sessionId;
        this.attributes = new HashMap<>();
    }

    public static Session createSession(String sessionId) {
        Session session = new Session(sessionId);
        sessions.put(sessionId, session);
        return session;
    }

    public static Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }
}
