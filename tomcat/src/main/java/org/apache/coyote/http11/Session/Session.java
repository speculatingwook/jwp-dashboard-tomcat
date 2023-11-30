package org.apache.coyote.http11.Session;

import nextstep.jwp.model.User;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final String id;
    private static Map<String, Object> session = new HashMap<>();
    public Session(final String id) {
        this.id = id;
    }

    public static void setSession(String sessionId, Object user) {
        session.put(sessionId, user);
    }

    public static Object getSession(String sessionId) {
        return session.get(sessionId);
    }

    public String getId() {
        return id;
    }

}
