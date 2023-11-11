package org.apache.coyote.http11;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
        try (final var inputStream = connection.getInputStream(); final var outputStream = connection.getOutputStream())
        {
             //buffer reader 로 뽑으면 막힘이 없음 but inputStream.readAllBytes() 호출시 정지. -> 다른 곳에서도 input Stream 을 호출 ?

            final InputStream bufferedInputStream = new BufferedInputStream(inputStream);
            final InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
            final BufferedReader bf = new BufferedReader(inputStreamReader);

            final var response = outPutBuilder(bf.readLine());

            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
    public String outPutBuilder(String requestSourceHeader) throws IOException {
        String[] info = requestSourceHeader.split(" ");
        if(info[1].equals("/"))
        {
            final var responseBody = "Hello world!";
            final var response = String.join("\r\n",
                    "HTTP/1.1 200 OK ",
                    "Content-Type: text/html;charset=utf-8 ",
                    "Content-Length: " + responseBody.getBytes().length + " ",
                    "",
                    responseBody);
            return response;
        }
        if(info[1].equals("/index.html")){
            final URL resource = getClass().getClassLoader().getResource("static/index.html");
            final Path path = new File(resource.getFile()).toPath();
            byte[] filesIO = Files.readAllBytes(path);
            String fileResponseData = new String(filesIO);

            final var response = String.join("\r\n",
                    "HTTP/1.1 200 OK ",
                    "Content-Type: text/html;charset=utf-8 ",
                    "Content-Length: " + filesIO.length+ " ",
                    "",
                    fileResponseData);

            return response;
        }
        return "404";
    }
}
