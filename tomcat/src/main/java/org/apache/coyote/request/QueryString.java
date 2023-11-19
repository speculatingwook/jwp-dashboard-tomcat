package org.apache.coyote.request;

import java.util.HashMap;
import java.util.Map;

public class QueryString {
    private Map<String,String> queryStringMap;

    public QueryString() {
        this.queryStringMap = new HashMap<>();
    }

    public void put(String key, String value) {
        queryStringMap.put(key, value);
    }

    public String get(String key){
        return queryStringMap.get(key);
    }
    public boolean hasQueryStrings(){
        return !queryStringMap.isEmpty();
    }

    @Override
    public String toString() {
        return queryStringMap.toString();
    }
}
