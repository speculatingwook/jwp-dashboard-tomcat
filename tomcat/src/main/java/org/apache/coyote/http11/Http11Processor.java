package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
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
        try{
            if (isUrlIndex(connection)) {
                getResourse(connection, getUrl(connection));
            }else {
                none(connection);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void none(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream()) {

            final var responseBody = "Hello world!";

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
        }
    }
    private void getResourse(final Socket connection, String url) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream()) {

            final URL resource = getClass().getClassLoader().getResource("static"+ url);
            final var responseBody = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));

            final var response = String.join("\r\n",
                    "HTTP/1.1 200 OK ",
                    "Content-Type: text/html;charset=utf-8 ",
                    "Content-Length: " + responseBody.getBytes(StandardCharsets.UTF_8).length + " ",
                    "",
                    responseBody);
            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    public boolean isUrlIndex(Socket connection) throws IOException {
        try{
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            String requestLine = bufferedReader.readLine();
            ArrayList<String> requestLineList = Arrays.stream(requestLine.split(" ")).collect(Collectors.toCollection(ArrayList::new));
            return requestLineList.get(1).equals("/index.html");
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
    public String getUrl(Socket connection) throws IOException {
        try{
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            String requestLine = bufferedReader.readLine();
            ArrayList<String> requestLineList = Arrays.stream(requestLine.split(" ")).collect(Collectors.toCollection(ArrayList::new));
            return requestLineList.get(1);
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }
}