package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.util.ResourceFinder;
import org.apache.coyote.Processor;
import org.apache.util.HttpResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    private final ControllerMapper controllerMapper = new ControllerMapper();
    private final ResourceFinder resourceFinder = new ResourceFinder();

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

            // HTTP 요청 메시지 파싱
            String requestLine = parsingHttpRequestMessage(inputStream);

            String url = parsingUrl(requestLine);
            String method = parsingMethod(requestLine);

            Response response = new Response();

            // 데이터 가공이 필요한 지 단순 자원 요청인 지 검사
            if(url.contains(".")) {
                response = new Response(
                        HttpResponseCode.OK.toString(),
                        HttpResponseCode.OK.getReasonPhrase(),
                        resourceFinder.getContentType(resourceFinder.getFileExtension(url)),
                        resourceFinder.getResource(url));
            } else {
                Object controller = controllerMapper.mappingController(url);
                response = delegateController(controller, method, url);

            }

            outputStream.write(response.toString().getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    private Response delegateController(Object controller, String method, String url) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleRequestMethod = controller.getClass().getMethod("handleRequest", String.class, String.class);
        return (Response) handleRequestMethod.invoke(controller, method, url);
    }

    private String parsingHttpRequestMessage(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.readLine();
    }

    private String parsingUrl(String requestLine) throws MalformedURLException {
        String[] requestParts = requestLine.split(" ");
        return requestParts[1];
    }

    private String parsingMethod(String requestLine) throws MalformedURLException {
        String[] requestParts = requestLine.split(" ");
        return requestParts[0];
    }
}