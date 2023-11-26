package org.apache.coyote.http11;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSessionManager {
    private static Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

    public static HttpSession getSession(String id) {
        HttpSession session = sessions.get(id);

        if (session == null) {
            session = new HttpSession(id);
            sessions.put(id, session);
            return session;
        }
        return session;
    }

    static void remove(String id) {
        sessions.remove(id);
    }

}
