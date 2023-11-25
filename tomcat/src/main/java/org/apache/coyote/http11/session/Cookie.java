package org.apache.coyote.http11.session;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Cookie {
    private Map<String, String> values;
    private final String JSESSIONID = "JSESSIONID";

    public Cookie() {
        values = new HashMap<>();
    }

    public void putValue(String name, String value) {
        values.put(name, value);
    }

    public String getJSessionId() {
        return values.get(JSESSIONID);
    }
}

