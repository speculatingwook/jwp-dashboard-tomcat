package org.apache.coyote.http11;

import nextstep.jwp.conrtroller.LoginController;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.model.User;
import org.apache.coyote.Processor;
import org.apache.util.HttpResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.*;
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

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String requestLine = reader.readLine();

            // -- test --
            String requestMethod = getRequestMethod(requestLine);
            String requestUrl = getRequestUrl(requestLine);

            Response response = controllerMapping(requestMethod, requestUrl);


            // todo 메소드 분리하고 첫'/' 문자 뒤에 어떤 단어가 오는 지 검사 하기
            // 확장자가 없을 수도 있음 -> /부터 '확장자가 있는 지 검사할 것
            // 1. index.html
            // 2. login?
            // 3. login


            int contentLength = 0;
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.substring("Content-Length:".length()).trim());
                }
            }

            String requestPayload = "";

            if (contentLength > 0) {
                char[] requestBody = new char[contentLength];
                int bytesRead = reader.read(requestBody, 0, contentLength);
                requestPayload = new String(requestBody, 0, bytesRead);
            }

            Response response = generateResponse(requestLine,requestPayload);

            outputStream.write(response.toString().getBytes());

            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String getRequestMethod(String requestLine) {
        return requestLine.split(" ")[0];
    }

    private String getRequestUrl(String requestLine) {
        return requestLine.split(" ")[1];
    }

    private Response controllerMapping(String requestMethod, String requestUrl) {
        ArrayList<String> domains = new ArrayList<>();
        domains.add("index");
        domains.add("login");
        domains.add("register");

        // 기본 응답 생성
        Response response = new Response("", "", "", "");

        for (String domain : domains) {
            if (requestUrl.contains(domain)) {
                String controllerClassName = domain.substring(0, 1).toUpperCase() + domain.substring(1) + "Controller";

                try {
                    Class<?> controllerClass = Class.forName("your.package.name." + controllerClassName);
                    Constructor<?> constructor = controllerClass.getConstructor(String.class, String.class);
                    Object controllerInstance = constructor.newInstance(requestMethod, requestUrl);

                    Method generateResponseMethod = controllerClass.getMethod("generateResponse");
                    response = (Response) generateResponseMethod.invoke(controllerInstance);
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                         InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return response;
    }

    private URL getResource(String requestURL) {
        if(requestURL.equals("/")) {
            return getClass().getClassLoader().getResource("static/index.html");
        }

        ArrayList<String> pageList = new ArrayList<>();
        pageList.add("/login");
        pageList.add("/register");

        Optional<String> matchingPage = pageList.stream()
                .filter(page -> requestURL.contains(page))
                .map(page -> requestURL + ".html")
                .findFirst();

        return matchingPage
                .map(pageName -> getClass().getClassLoader().getResource("static/" + pageName))
                .orElseGet(() -> getClass().getClassLoader().getResource("static/" + requestURL));
    }

    private String getContentType(URL resource) {
        String contentType = URLConnection.guessContentTypeFromName(resource.getFile());

//        if (contentType == null) {
//            contentType = URLConnection.guessContentTypeFromStream(java.nio.file.Files.newInputStream();
//        }

        return contentType;
    }

    private Response generateResponse(String requestLine, String requestPayload) throws IOException {
        final String requestURL = getRequestUrl(requestLine);
        final URL resource = getResource(requestURL);
        final String fileContent;

        final String responseCode;
        final String responseStatus;

        if(getRequestMethod(requestLine) == "POST") {
            if(register(parseQueryString(requestPayload))) {
                responseCode = String.valueOf(HttpResponseCode.UNAUTHORIZED.getCode());
                responseStatus = HttpResponseCode.UNAUTHORIZED.getReasonPhrase();
                final URL unauthorizedResource = getClass().getClassLoader().getResource("static/I.html");
                fileContent = new String(Files.readAllBytes(new File(unauthorizedResource.getFile()).toPath()));

                return new Response(responseCode, responseStatus, "", fileContent);
            }
        }

        if(requestURL.contains("?")) {
            String queryString = extractQueryString(requestURL);
            Map<String, String> parameters = parseQueryString(queryString);

            if (requestURL.contains("/login")) {
                if(login(parameters)) {
                    responseCode = String.valueOf(HttpResponseCode.OK.getCode());
                    responseStatus = HttpResponseCode.OK.getReasonPhrase();
                    final URL indexResource = getClass().getClassLoader().getResource("static/index.html");
                    final String contentType = getContentType(indexResource);
                    fileContent = new String(Files.readAllBytes(new File(indexResource.getFile()).toPath()));

                    return new Response(responseCode, responseStatus, contentType, fileContent);
                } else {
                    responseCode = String.valueOf(HttpResponseCode.UNAUTHORIZED.getCode());
                    responseStatus = HttpResponseCode.UNAUTHORIZED.getReasonPhrase();
                    final URL unauthorizedResource = getClass().getClassLoader().getResource("static/401.html");
                    fileContent = new String(Files.readAllBytes(new File(unauthorizedResource.getFile()).toPath()));

                    return new Response(responseCode, responseStatus, "", fileContent);
                }
            }
        }

        if(resource == null) {
            responseCode = String.valueOf(HttpResponseCode.NOT_FOUND.getCode());
            responseStatus = HttpResponseCode.NOT_FOUND.getReasonPhrase();

            final URL notFoundResource = getClass().getClassLoader().getResource("static/404.html");
            fileContent = new String(Files.readAllBytes(new File(notFoundResource.getFile()).toPath()));

            return new Response(responseCode, responseStatus, "", fileContent);
        } else {
            responseCode = String.valueOf(HttpResponseCode.OK.getCode());
            responseStatus = HttpResponseCode.OK.getReasonPhrase();

            final String contentType = getContentType(resource);
            fileContent = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));

            return new Response(responseCode, responseStatus, contentType, fileContent);
        }
    }

    private String extractQueryString(String requestURL) {
        int index = requestURL.indexOf("?");
        return requestURL.substring(index + 1);
    }

    private Map<String, String> parseQueryString(String queryString) {
        return Arrays.stream(queryString.split("&"))
                .map(parameter -> parameter.split("="))
                .collect(Collectors.toMap(
                        parameter -> parameter[0],
                        parameter -> (parameter.length > 1 ? parameter[1] : "")
                ));
    }

    private boolean login(Map<String, String> parameters) {
        Optional<User> optionalUser = InMemoryUserRepository.findByAccount(parameters.get("account"));
        User findUser = optionalUser.orElseThrow( () -> new RuntimeException());

        return findUser.checkPassword(parameters.get("password"));
    }

    private boolean register(Map<String, String> parameters) {
        Optional<User> optionalUser = InMemoryUserRepository.findByAccount(parameters.get("account"));

        if(optionalUser.isPresent()) {
            log.info("중복된 account는 사용할 수 없습니다.");
            return false;
        } else {
            InMemoryUserRepository.save(new User(parameters.get("account"), parameters.get("password"), parameters.get("email")));
            return true;
        }
    }

}