package org.apache.coyote.session;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cookie {
    private Map<String, String> cookies;

    public Cookie(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public static Cookie parseCookie(String cookies) {
        Map<String, String> cookieMap = new HashMap<>();
        if (cookies == null) {
            return new Cookie(cookieMap);
        }
        String[] splitCookies = cookies.split("; ");
        Arrays.stream(splitCookies)
                .forEach(keyVal -> {
                    String[] keyValSplit = keyVal.split("=");
                    cookieMap.put(keyValSplit[0], keyValSplit[1]);
                });
        return new Cookie(cookieMap);
    }

    public String getCookie(String key) {
        return cookies.getOrDefault(key, null);
    }

    public void addCookie(String key, String val) {
        cookies.put(key, val);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = cookies.keySet();
        for (String key : keySet) {
            sb.append(key)
                    .append("=")
                    .append(cookies.get(key))
                    .append(";");
        }
        return sb.toString();
    }
}
