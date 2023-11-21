package org.apache.coyote.request;

import javax.naming.directory.NoSuchAttributeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.Arrays;

public class HttpRequest {
    private RequestParam requestParam;
    private String path;
    private String httpMethod;
    private HttpRequestHeader header;

    private Cookie cookie;

    public HttpRequest(Reader reader) throws NoSuchAttributeException, IOException {
        requestParam = new RequestParam();
        header = new HttpRequestHeader();

        //첫줄은 HTTP 메소드와 URI
        BufferedReader br = (BufferedReader) reader;
        parseRequestLine(br);
        parseRequestHeader(br);
        parseRequestBody(br);
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public RequestParam getRequestParam() {
        return requestParam;
    }
    public String getCookie() {
        return header.getHeader("Cookie");
    }

    public Object getAttribute(String key) {
        return requestParam.get(key);
    }

    private void parseRequestLine(BufferedReader br) throws IOException, NoSuchAttributeException {
        //첫줄 Method & URI parsing
        String firstLine = br.readLine();
        if (firstLine == null)
            throw new NoSuchAttributeException("요청이 잘못 들어옴");

        System.out.println("firstLine = " + firstLine);

        String[] methodPathSplit = firstLine.split(" ");
        this.httpMethod = methodPathSplit[0];
        this.path = methodPathSplit[1];
        System.out.println("path = " + path);
        System.out.println("httpMethod = " + httpMethod);
        //parse QueryString and uri
        if (methodPathSplit[1].contains("?")) {
            String[] uriQueryString = methodPathSplit[1].split("\\?");
            this.path = uriQueryString[0];

            //parse QueryString Key & Value
            parseQueryString(uriQueryString[1]);
        }
    }

    private void parseQueryString(String queryString) {
        String[] queryStringArray = queryString.split("&");
        Arrays.stream(queryStringArray)
                .forEach(keyVal -> {
                    String[] keyValSplit = keyVal.split("=");
                    requestParam.put(keyValSplit[0], keyValSplit[1]);
                });
    }

    private void parseRequestHeader(BufferedReader br) throws IOException {
        String header = "";
        while (!(header = br.readLine()).isEmpty()) {
            System.out.println("header = " + header);
            String[] keyVal = header.split(": ");
            this.header.addHeader(keyVal[0], keyVal[1]);
        }
    }

    private void parseRequestBody(BufferedReader br) throws IOException {
        String length = header.getHeader("Content-Length");
        if (length == null) {
            return;
        }

        int contentLength = Integer.parseInt(length);
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        String bodyString = URLDecoder.decode(new String(body),"UTF-8");

        String[] bodyStringArray = bodyString.split("\\&");
        Arrays.stream(bodyStringArray)
                .forEach(keyVal -> {
                    String[] keyValSplit = keyVal.split("=");
                    requestParam.put(keyValSplit[0], keyValSplit[1]);
                });
        System.out.println(requestParam);
    }
    @Override
    public String toString() {
        return requestParam + "\n" +
                path + "\n" +
                httpMethod +
                header;
    }

}
