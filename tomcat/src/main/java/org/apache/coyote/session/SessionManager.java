package org.apache.coyote.session;

import nextstep.util.Constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static Map<String, Session> sessionMap = new HashMap<>();

    private SessionManager() {
    }

    public static Session getSession(String jSessionId) {

        if (jSessionId == null) {
            return createSession();
        }
        return sessionMap.getOrDefault(jSessionId, null);
    }

    public static Session createSession() {
        Session session = new Session();
        sessionMap.put(session.getSessionId(), session);
        return session;
    }
}
