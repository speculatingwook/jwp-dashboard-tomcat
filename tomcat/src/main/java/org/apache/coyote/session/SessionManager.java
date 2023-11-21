package org.apache.coyote.session;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private Map<String, Session> sessionMap;

    private final SessionManager sessionManager = new SessionManager();

    public static SessionManager getSessionManager(){
        return this.sessionManager;
    }

    private SessionManager() {
        this.sessionMap = new HashMap<>();
    }

    public Session getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public void addSessionMap(String sessionId, Session session) {
        sessionMap.put(sessionId, session);
    }
}
