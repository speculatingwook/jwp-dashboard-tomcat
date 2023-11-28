package org.apache.coyote.session;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
}
