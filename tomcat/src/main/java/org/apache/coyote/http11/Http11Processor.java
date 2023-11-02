package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.httprequest.HttpMethod;
import org.apache.coyote.http11.httprequest.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

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
             final var reader = new InputStreamReader(inputStream);
             final var bufferedReader = new BufferedReader(reader)) {

            HttpRequest httpRequest = HttpRequest.parse(bufferedReader);
            System.out.println(httpRequest.getPath()); // To Debug

            if (httpRequest.getMethod() == HttpMethod.GET) {
                String responseBody;
                try {
                    responseBody = responseBodyFromPath(httpRequest.getPath());
                } catch (Exception e) {
                    responseBody = errorHandler();
                }
                final var response = String.join("\r\n",
                        "HTTP/1.1 200 OK ",
                        "Content-Type: text/html;charset=utf-8 ",
                        "Content-Length: " + responseBody.getBytes().length + " ",
                        "",
                        responseBody);

                outputStream.write(response.getBytes());
                outputStream.flush();
            }

            else {
                final var responseBody = "Hello world!";
                final var response = String.join("\r\n",
                        "HTTP/1.1 200 OK ",
                        "Content-Type: text/html;charset=utf-8 ",
                        "Content-Length: " + responseBody.getBytes().length + " ",
                        "",
                        responseBody);

                outputStream.write(response.getBytes());
                outputStream.flush();
            }
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String responseBodyFromPath(String filePath) throws Exception {
        try {
            URL resource = this.getClass()
                    .getClassLoader()
                    .getResource("static" + filePath);
            Path path = Paths.get(Objects.requireNonNull(resource).toURI());
            String responseBody = new String(Files.readAllBytes(path));
            return responseBody;
        } catch (NullPointerException | URISyntaxException | IOException e) {
            throw new Exception("404 Not Found");
        }
    }
}
