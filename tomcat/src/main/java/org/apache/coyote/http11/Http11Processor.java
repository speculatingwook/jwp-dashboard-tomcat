package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
             final var outputStream = connection.getOutputStream();
             final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String responseBody = "Hello world!";
            String contentType = "text/html";

            final String requestUrl = readRequestUrl(bufferedReader);

            if (requestUrl.equals("/")) {
                contentType = makeContentType(contentType, requestUrl);
                responseBody = new String(readDefaultFile(), StandardCharsets.UTF_8);
            }

            if (requestUrl.contains(".html") || requestUrl.contains(".css") || requestUrl.contains(".js")) {
                contentType = makeContentType(contentType, requestUrl);
                responseBody = new String(readAllFile(requestUrl), StandardCharsets.UTF_8);
            }


            String response = String.join("\r\n",
                    "HTTP/1.1 200 OK ",
                    "Content-Type: " + contentType + ";charset=utf-8 ",
                    "Content-Length: 5564 ",
                    "",
                    responseBody);

            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String makeContentType(String contentType, final String requestUrl) {
        if (requestUrl.contains(".css")) {
            contentType = "text/" + requestUrl.split("\\.")[1];
        }
        if (requestUrl.contains(".js")) {
            contentType = "application/javascript";
        }
        return contentType;
    }

    private static String readRequestUrl(final BufferedReader bufferedReader) throws IOException {
        final String httpStartLine = bufferedReader.readLine();
        return httpStartLine.split(" ")[1];
    }

    private static byte[] readAllFile(final String requestUrl) throws IOException, URISyntaxException {
        final URL resourceUrl = ClassLoader.getSystemResource("static" + requestUrl);
        final Path path = Path.of(resourceUrl.toURI());
        return Files.readAllBytes(path);
    }

    private static byte[] readDefaultFile() throws IOException, URISyntaxException {
        final URL resourceUrl = ClassLoader.getSystemResource("static/index.html");
        final Path path = Path.of(resourceUrl.toURI());
        return Files.readAllBytes(path);
    }
}
