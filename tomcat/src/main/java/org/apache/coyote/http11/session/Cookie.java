package org.apache.coyote.http11.session;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    private Map<String, String> values;

    public Cookie() {
        values = new HashMap<>();
    }

    public void setValue(String key, String value) {
        values.put(key, value);
    }

    public String getValue(String key) {
        return values.get(key);
    }
}

