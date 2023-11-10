package org.apache.coyote.http11;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

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

            final BufferedReader request = new BufferedReader(new InputStreamReader(inputStream));
            final String[] contexts = request.readLine().split(" ");
            final String method = contexts[0];
            final String path = contexts[1];
            final String fileType;
            if (path.contains(".")) {
                fileType = path.substring(path.lastIndexOf(".") + 1);
            } else {
                fileType = "html";
            }
            final String responseBody;
            if (Objects.equals(path, "/")) {
                responseBody = "Hello world!";
            } else {
                URL resource = getClass().getClassLoader()
                    .getResource(String.format("static/%s", path));
                assert resource != null;
                responseBody = new String(
                    Files.readAllBytes(new File(resource.getFile()).toPath()));
            }
            final var response = String.join("\r\n",
                    "HTTP/1.1 200 OK ",
                    String.format("Content-Type: text/%s;charset=utf-8 ", fileType),
                    "Content-Length: " + responseBody.getBytes().length + " ",
                    "",
                    responseBody);

            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
}
