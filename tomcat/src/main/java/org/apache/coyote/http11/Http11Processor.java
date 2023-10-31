package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
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
             final var outputStream = connection.getOutputStream()) {

            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String requestLine = bufferedReader.readLine();
            ArrayList<String> requestLineList = Arrays.stream(requestLine.split(" ")).collect(Collectors.toCollection(ArrayList::new));

            if (Objects.equals(requestLineList.get(1), "/index.html")) {
                FileInputStream fileStream = new FileInputStream("tomcat/src/main/resources/static/index.html");
                InputStreamReader fileReader = new InputStreamReader(fileStream);
                BufferedReader fileBufferedReader =  new BufferedReader(fileReader);
                StringBuilder fileContent = new StringBuilder();
                String line;
                while ((line = fileBufferedReader.readLine()) != null) {
                    fileContent.append(line);
                }
                fileBufferedReader.close();
                fileReader.close();
                fileStream.close();
                String responseBody = fileContent.toString();
                final var response = String.join("\r\n",
                        "HTTP/1.1 200 OK ",
                        "Content-Type: text/html;charset=utf-8 ",
                        "Content-Length: " + responseBody.getBytes().length + " ",
                        "",responseBody);
                outputStream.write(response.getBytes());
            }else {
                final var responseBody = "Hello world!";
                final var response = String.join("\r\n",
                        "HTTP/1.1 200 OK ",
                        "Content-Type: text/html;charset=utf-8 ",
                        "Content-Length: " + responseBody.getBytes().length + " ",
                        "",
                        responseBody);
                outputStream.write(response.getBytes());
            }
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
}
