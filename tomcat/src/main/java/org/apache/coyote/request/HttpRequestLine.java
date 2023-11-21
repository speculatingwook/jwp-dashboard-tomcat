package org.apache.coyote.request;

import javax.naming.directory.NoSuchAttributeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestLine {
    private String path;
    private String httpMethod;

    private String queryString;

    private String httpVersion;

    public String getPath() {
        return path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    private HttpRequestLine(String path, String httpMethod, String queryString, String httpVersion) {
        this.path = path;
        this.httpMethod = httpMethod;
        this.queryString = queryString;
        this.httpVersion = httpVersion;
    }

    public static HttpRequestLine parse(BufferedReader br) throws IOException, NoSuchAttributeException {
        //첫줄 Method & URI parsing
        String firstLine = br.readLine();
        if (firstLine == null)
            throw new NoSuchAttributeException("요청이 잘못 들어옴");
        String[] methodPathSplit = firstLine.split(" ");

        String httpMethod = methodPathSplit[0];
        String path = methodPathSplit[1];
        String version = methodPathSplit[2];

        String retQueryString = "";
        //parse QueryString and uri
        if (methodPathSplit[1].contains("?")) {
            String[] uriQueryString = methodPathSplit[1].split("\\?");
            path = uriQueryString[0];
            retQueryString = uriQueryString[1];
        }
        return new HttpRequestLine(path, httpMethod, retQueryString, version);
    }

    //parse QueryString Key & Value
    public Map<String, String> getQueryStringMap() {
        if(queryString.isEmpty()){
            return new HashMap<>();
        }

        Map<String, String> queryStringMap = new HashMap<>();
        String[] queryStringArray = queryString.split("&");
        Arrays.stream(queryStringArray)
                .forEach(keyVal -> {
                    String[] keyValSplit = keyVal.split("=");
                    queryStringMap.put(keyValSplit[0], keyValSplit[1]);
                });

        return queryStringMap;
    }
}
