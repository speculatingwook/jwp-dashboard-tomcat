package org.apache.coyote.request;

import javax.naming.directory.NoSuchAttributeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class HttpRequest {
    private QueryString queryString;
    private String path;
    private String httpMethod;

    private HttpRequestHeader header;
    public HttpRequest(Reader reader) throws NoSuchAttributeException, IOException {
        queryString = new QueryString();
        header = new HttpRequestHeader();

        //첫줄은 HTTP 메소드와 URI
        parseMethodAndPath((BufferedReader) reader);
        parseHeader(reader);
    }

    public String getPath() {
        return path;
    }

    public String getHttpMethod(){
        return httpMethod;
    }

    public void addQueryStringValue(String key, String value) {
        queryString.addQueryString(key, value);
    }

    private void parseMethodAndPath(BufferedReader br) throws IOException, NoSuchAttributeException {
        //첫줄 Method & URI parsing
        String firstLine = br.readLine();
        if (firstLine == null)
            throw new NoSuchAttributeException("없음");

        String[] methodPathSplit = firstLine.split(" ");
        this.httpMethod = methodPathSplit[0];

        this.path = methodPathSplit[1];
        //parse QueryString and uri
        if (methodPathSplit[1].contains("?")) {
            String[] uriQueryString = methodPathSplit[1].split("\\?");
            this.path = uriQueryString[0];

            //parse QueryString Key & Value
            parseQueryString(uriQueryString[1]);
        }

    }

    private void parseQueryString(String queryString) {
        String[] queryStringArray = queryString.split("\\&");
        Arrays.stream(queryStringArray)
                .forEach(keyVal -> {
                    String[] keyValSplit = keyVal.split("=");
                    this.addQueryStringValue(keyValSplit[0], keyValSplit[1]);
                });
    }

    public void parseHeader(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        while (br.ready()) {
            String header = br.readLine();
            if (header.contains(": ")) {
                String[] keyVal = header.split(": ");
                this.header.addHeader(keyVal[0], keyVal[1]);
            }
        }
    }


    @Override
    public String toString() {
        return "HttpRequest{" +
                "queryString=" + queryString +
                ", uri='" + path + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", header=" + header +
                '}';
    }
}
