package org.apache.coyote.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {

    private Map<String, String> header;

    public HttpRequestHeader() {
        header = new HashMap<>();
    }

    private HttpRequestHeader(Map<String, String> header) {
        this.header = header;
    }

    public static HttpRequestHeader parse(BufferedReader br) throws IOException {
        Map<String,String> headerrMap= new HashMap<>();
        String header = "";
        while (!(header = br.readLine()).isEmpty()) {
            System.out.println("header = " + header);
            String[] keyVal = header.split(": ");
            headerrMap.put(keyVal[0], keyVal[1]);
        }
        return new HttpRequestHeader(headerrMap);
    }

    public String getHeader(String key){
        return header.getOrDefault(key, null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        header.keySet().forEach(
                (k) -> sb.append("\n"+k + ": " +header.get(k))
        );
        sb.append("\n");
        return sb.toString();
    }
}
