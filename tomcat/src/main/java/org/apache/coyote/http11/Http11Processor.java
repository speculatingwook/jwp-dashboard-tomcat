package org.apache.coyote.http11;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
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
        try (final var inputStream = connection.getInputStream(); final var outputStream = connection.getOutputStream()) {
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
    public String responseBuilder(String requestSourcePath) throws IOException {
        String contentTypeFile = "text/html;";
        Integer responseHeaderCode = 200;
        String responseHeaderMessage = "OK";
        if (requestSourcePath.equals("/")) {
            requestSourcePath ="/Home.txt";
        }
        else if(requestSourcePath.contains(".html") || requestSourcePath.contains("/js/scripts.js") || requestSourcePath.contains("/assets")||requestSourcePath.contains(".css")) {
            contentTypeFile ="text/"+requestSourcePath.substring(requestSourcePath.lastIndexOf(".")+1)+";";
        }
        else {
            requestSourcePath ="/404.html";
            responseHeaderCode = 404;
            responseHeaderMessage ="NOT FOUND";
        }
        URL resource = getClass().getClassLoader().getResource("static"+requestSourcePath);
        final Path path = new File(resource.getFile()).toPath();
        byte[] filesIO = Files.readAllBytes(path);
        final String fileResponseData = new String(filesIO);
        final var contentLength = filesIO.length;
        final var response = String.join("\r\n",
                "HTTP/1.1 "+responseHeaderCode+" "+responseHeaderMessage+" ",
                "Content-Type: "+contentTypeFile+"charset=utf-8 ",
                "Content-Length: " +contentLength+ " ",
                "",
                fileResponseData);

        return response;

    }
    public String outPutBuilder(String requestSourceHeader) throws IOException {
        String[] info = requestSourceHeader.split(" ");

        return responseBuilder(info[1]);
    }
}
