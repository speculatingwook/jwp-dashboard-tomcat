package org.apache.coyote.http11.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ParseUtil {
    public static Map<String, String> parseQueryParameters(String query) throws UnsupportedEncodingException {
        query = query.replace("\r\n", "");
        String[] params = query.split("&");

        Map<String, String> queryParameters = new HashMap<>();

        for (String param : params) {
            String[] keyValue = param.split("=");

            if (keyValue.length == 2) {
                String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                queryParameters.put(key, value);
            }
        }
        return queryParameters;
    }
}
