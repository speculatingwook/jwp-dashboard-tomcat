package org.apache.coyote.request;

import java.util.HashMap;
import java.util.Map;

public class RequestParam {
    private Map<String,String> requestParams;

    public RequestParam() {
        this.requestParams = new HashMap<>();
    }

    public void put(String key, String value) {
        requestParams.put(key, value);
    }

    public String get(String key){
        return requestParams.getOrDefault(key,null);
    }
    public boolean hasRequestParam(){
        return !requestParams.isEmpty();
    }

    @Override
    public String toString() {
        return requestParams.toString();
    }
}
