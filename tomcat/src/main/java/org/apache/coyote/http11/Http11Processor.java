package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
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
                        getContentType(getFileExtension(url)),
                        getResource(url));
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

    public String getResource(String url) throws IOException {
        URL resource = getClass().getClassLoader().getResource("static" + url);
        return new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
    }

    public String getFileExtension(String url) {
        int lastDotIndex = url.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return url.substring(lastDotIndex + 1);
        }
        return "";
    }

    public String getContentType(String fileExtension) {
        switch (fileExtension) {
            case "html":
                return "text/html;charset=utf-8";
            case "js":
                return "application/javascript";
            case "css":
                return "text/css";
            case "jpg":
                return "image/jpeg";
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            // 다른 확장자에 대한 MIME 타입 추가
            default:
                return "application/octet-stream"; // 알 수 없는 확장자의 경우 기본값 설정
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