package org.apache.coyote.http11;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    private final String CookieString;
    private final Map<String, String> parameters;

    public Cookie(String cookieString) {
        this.CookieString =cookieString;
        this.parameters = getParameters(cookieString);
    }

    public String getCookieString() {
        return CookieString;
    }

    private Map<String, String> getParameters(String line) {
        Map<String, String> parameters = new HashMap<>();
        String[] data = line.split(":");
        String[] parameterList = data[1].split("; ");
        for (String param : parameterList) {
            String[] keyValuePair = param.split("=");
            parameters.put(keyValuePair[0], keyValuePair[1]);
        }
        return parameters;
    }

    public String getValue(String key) {
        String value = parameters.get(key);
        if (value != null) {
            System.out.println(value);
            return value;
        }
        return "";
    }

    public boolean isKeyExist(String key) {
        return parameters.containsKey(key);
    }
    public void setJSessionId() {
        parameters.put("JSESSIONID","656cef62-e3c4-40bc-a8df-94732920ed46");
    }

    public String toRaw(String key) {
        return key + "=" + parameters.get(key);
    }
}
