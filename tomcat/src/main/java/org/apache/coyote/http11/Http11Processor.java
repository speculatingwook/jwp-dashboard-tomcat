package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.request.QueryString;
import nextstep.jwp.request.Request;
import nextstep.jwp.response.Response;
import nextstep.jwp.service.LoginService;

import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

            Request request = createRequest(inputStream);
            String fileName = request.getFileName();
            String queryString = request.getQueryString();

            String contentType = getContentType(fileName);
            String responseBody = getResponseBody(fileName, queryString);

            Response response = createResponse(contentType, responseBody);
            outputStream.write(response.getResponse().getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Request createRequest(InputStream inputStream) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String path = reader.readLine().split(" ")[1];
        return new Request(path);
    }

    public String getContentType(String fileName) {
        String contentType = "text/html";
        if (fileName.contains(".css")) contentType = "text/css";
        return contentType;
    }

    public String getResponseBody(String fileName, String queryString) throws IOException {
        if(fileName.equals("/")){
            return "Hello world!";
        }
        String resourcePath = getResourcePath(fileName);

        if (fileName.equals("/login") && !queryString.isEmpty()) {
            QueryString qs = new QueryString(queryString);
            String loginResponse = LoginService.login(qs.toParsing());
            if (!loginResponse.equals("/login.html")) {
                return loginResponse;
            }
        }

        final URL resource = getClass().getClassLoader().getResource(resourcePath);
        return new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
    }

    public String getResourcePath(String fileName) {
        String resourcePath = "static" + fileName;
        if(!fileName.contains(".")) resourcePath = "static" + fileName + ".html";
        return resourcePath;
    }

    public Response createResponse(String contentType, String responseBody) {
        return new Response(contentType, responseBody);
    }
}
