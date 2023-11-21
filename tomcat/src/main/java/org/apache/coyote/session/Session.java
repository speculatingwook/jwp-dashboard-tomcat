package org.apache.coyote.session;

import java.util.Map;
import java.util.UUID;

public class Session {
    private String sessionId;
    private Map<String,Object> attributes;

    public Session() {
        sessionId = UUID.randomUUID().toString();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAttribute(String key, Object val) {
        this.attributes.put(key, val);
    }

    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }
}
