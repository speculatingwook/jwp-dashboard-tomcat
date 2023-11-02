package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.apache.util.HttpResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String requestLine = reader.readLine();

            String response = generateResponse(requestLine);

            outputStream.write(response.getBytes());

            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    String getRequestMethod(String requestLine) {
        return requestLine.split(" ")[0];
    }

    String getRequestUrl(String requestLine) {
        return requestLine.split(" ")[1];
    }

    String getContentType(String requestURL) {
        if(requestURL.endsWith("html")) {
            return "text/html;charset=utf-8";
        } else {
            return "text/css;charset=utf-8";
        }
    }

    String generateResponse(String requestLine) throws IOException {
        String requestURL = getRequestUrl(requestLine);
        String contentType = getContentType(requestURL);
        final URL resource = getClass().getClassLoader().getResource("static/" + requestURL);

        String fileContent = "";
        String responseCode = "";
        String responseStatus = "";

        if (resource != null) {
            fileContent = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
            responseCode = String.valueOf(HttpResponseCode.OK.getCode());
            responseStatus = HttpResponseCode.OK.getReasonPhrase();
        } else {
            responseCode = String.valueOf(HttpResponseCode.NOT_FOUND.getCode());
            responseStatus = HttpResponseCode.NOT_FOUND.getReasonPhrase();

            final URL notFoundResource = getClass().getClassLoader().getResource("static/404.html");
            fileContent = new String(Files.readAllBytes(new File(notFoundResource.getFile()).toPath()));
        }

        String response = "HTTP/1.1 " + responseCode + " " + responseStatus + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + fileContent.length() + " \r\n" +
                "\r\n" +
                fileContent;

        // 리다이렉션 로직 추가
        if (responseCode.equals(String.valueOf(HttpResponseCode.NOT_FOUND.getCode()))) {
            response = "HTTP/1.1 " + HttpResponseCode.MOVED_PERMANENTLY.getCode() + " " + HttpResponseCode.MOVED_PERMANENTLY.getReasonPhrase() + "\r\n" +
                    "Location: /404.html\r\n\r\n";
        }

        return response;
    }


}