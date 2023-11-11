package org.apache.coyote.http11.httprequest;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    private Map<String, String> cookies = new HashMap<>();

    public static Cookie parse(String cookieStr) {
        Cookie cookie = new Cookie();
        String[] cookieStrs = cookieStr.split("; ");
        for (String str : cookieStrs) {
            String[] keyValue = str.split("=");
            if (keyValue.length == 2) {
                cookie.addCookie(keyValue[0], keyValue[1]);
            } else {
                cookie.addCookie(keyValue[0], "");
            }
        }
        return cookie;
    }

    public void addCookie(String key, String value) {
        cookies.put(key, value);
    }

    public String getValue(String key) {
        return cookies.get(key);
    }
}
