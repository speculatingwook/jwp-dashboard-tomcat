package org.apache.coyote.http11.request;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import org.apache.coyote.http11.Http11Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestWrapper {

    private static final String EMPTY_REQUEST = "요청이 비어있습니다.";
    private static final Logger log = LoggerFactory.getLogger(HttpRequestWrapper.class);
    List<String> lines;
    String method;
    String path;
    String query;
    private Map<String, String> queryData;


    public HttpRequestWrapper(BufferedReader reader) throws IOException {
        List<String> lines = bufferReaderToLines(reader);
        if (lines == null || lines.isEmpty()) {
            throw new NoSuchElementException(EMPTY_REQUEST);
        }
        this.lines = lines;
        log.info(lines.toString());
        if(lines.get(0).contains("?")){
            String[] firstLineSplit = lines.get(0).split(" ");
            this.method = firstLineSplit[0];
            parseQueryString(firstLineSplit[1]);
        }
        else {
            String[] firstLineSplit = lines.get(0).split(" ");
            this.method = firstLineSplit[0];
            this.path = firstLineSplit[1];
        }

    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryData() {
        return queryData;
    }

    public String getLines() {
        String delimiter = "\n";
        return String.join(delimiter, lines);
    }
    private List<String> bufferReaderToLines(final BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line = " ";
        while (!line.isEmpty()) {
            line = reader.readLine();
            lines.add(line);
        }
        return lines;
    }

    private void parseQueryString(String urlString) {
        log.info("url String"+urlString);
        Map<String, String> parameters = new HashMap<>();
        String[] splitResult = urlString.split("[?]");
        this.query = splitResult[1];
        try {
            if (query != null) {
                String fileType = ".html";
                path = splitResult[0] + fileType;
                String[] keyValuePairs = query.split("&");
                for (String keyValue : keyValuePairs) {
                    String[] parts = keyValue.split("=");
                    if (parts.length == 2) {
                        parameters.put(parts[0], parts[1]);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        this.queryData = parameters;
    }

    private String parsePath(String path) {
        if (!path.contains(".html")) {
            String fileType = ".html";
            return path + fileType;
        }
        return path;
    }
}
