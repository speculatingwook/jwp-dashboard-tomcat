package org.apache.coyote.request;

import java.util.HashMap;
import java.util.Map;

public class QueryString {
    private Map<String,String> queryStringMap;

    public QueryString() {
        this.queryStringMap = new HashMap<>();
    }

    public void addQueryString(String key, String value) {
        queryStringMap.put(key, value);
    }

    @Override
    public String toString() {
        return queryStringMap.toString();
    }
}
