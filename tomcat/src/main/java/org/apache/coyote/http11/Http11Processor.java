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
            // dataInputStream은 deprecated 되었으니, BufferedReader 사용
            BufferedReader request = new BufferedReader(new InputStreamReader(inputStream));
            String[] contexts = request.readLine().split(" ");
            String method = contexts[0];
            String path = contexts[1];
            final String responseBody;

            if (Objects.equals(path, "/")) {
                responseBody = "Hello world!";
            } else {
                URL resource = getClass().getClassLoader()
                    .getResource(String.format("static/%s", path));
                if (resource == null) {
                    resource = getClass().getClassLoader().getResource("static/404.html");
                }
                assert resource != null;
                responseBody = new String(
                    Files.readAllBytes(new File(resource.getFile()).toPath()));
            }
            final var response = String.join("\r\n",
                    "HTTP/1.1 200 OK ",
                    "Content-Type: text/html;charset=utf-8 ",
                    "Content-Length: " + responseBody.getBytes().length + " ",
                    "",
                    responseBody);

            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
            try {
                URL resource = getClass().getClassLoader()
                    .getResource(String.format("static/%s", "500.html"));
                final String responseBody = new String(
                    Files.readAllBytes(new File(resource.getFile()).toPath()));
                String response = String.join("\r\n",
                        "HTTP/1.1 500 Internal Server Error ",
                        "Content-Type: text/html;charset=utf-8 ",
                        "Content-Length: "+ responseBody.getBytes().length + " ",
                    "",
                    responseBody);

                connection.getOutputStream().write(response.getBytes());
                connection.getOutputStream().flush();
            } catch (IOException ioException) {
                log.error(ioException.getMessage(), ioException);
            }

        }
    }
}
