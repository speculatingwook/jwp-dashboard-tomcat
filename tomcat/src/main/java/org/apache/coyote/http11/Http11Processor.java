package org.apache.coyote.http11;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.model.User;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
             final var outputStream = connection.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {


            String requestPath = ""; //path 저장
            Optional<User> authenticatedUser = Optional.empty(); //로그인 시 user

            //path 파싱
            String requestLine = reader.readLine();
            String[] requestLineParts = null;
            if (requestLine != null) {
                requestLineParts = requestLine.split(" ");
                if (requestLineParts.length >= 2) {
                    requestPath = requestLineParts[1]; // 요청된 URI (/index.html 등)
                }
            }

            //parameter 파싱
            int paramIndex = requestPath.indexOf('?');
            if (paramIndex != -1) {
                Map<String, String> params = parseParameters(requestPath.substring(paramIndex + 1));
                requestPath = requestPath.substring(0, paramIndex);
                // 아이디와 비밀번호 확인
                String account = params.get("account");
                String password = params.get("password");
                if (account != null && password != null) {
                    authenticatedUser = authenticate(account, password);
                }
            }

            //각 path, parameter에 따라 response 만들기
            final String responseBody;
            final String response;

            if (requestPath.equals("/css/styles.css")) {
                //css 일 때
                responseBody = processStaticResource(requestPath);
                response = String.join("\r\n",
                        "HTTP/1.1 200 OK ",
                        "Content-Type: text/css;charset=utf-8 ",
                        "Content-Length: " + responseBody.getBytes().length + " ",
                        "",
                        responseBody);

                outputStream.write(response.getBytes());
            } else {
                //html일 때
                if (requestPath.equals("/")) { // '/'로 접속 시
                    responseBody = "Hello world!";
                } else {
                    if (authenticatedUser.isPresent()) {
                        //회원 조회한 결과
                        responseBody = authenticatedUser.get().toString();
                    } else {
                        //login에 .html 붙여주기
                        if (requestPath.equals("/login")) {
                            requestPath += ".html";
                        }
                        responseBody = processStaticResource(requestPath);
                    }
                }
                response = String.join("\r\n",
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

    // 정적 자원을 읽어오기
    public String processStaticResource(final String resourcePath) throws IOException {
        File resourceFile = new File("/Users/hayoon/spring/jwp-dashboard-http-mission/tomcat/src/main/resources/static" + resourcePath);
        if (resourceFile.exists()) {
            byte[] content = Files.readAllBytes(Path.of(resourceFile.getAbsolutePath()));
            return new String(content);
        } else {
            String notFoundResponse = "Resource not found.";
            return notFoundResponse;
        }
    }

    //path에서 파라미터 파싱
    private static Map<String, String> parseParameters(String request) {
        System.out.println(request);
        Map<String, String> params = new HashMap<>();
        String[] lines = request.split("&");
        if (lines.length > 0) {
            for (String line : lines) {
                String[] keyValue = line.split("=");
                if (keyValue.length > 1) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }

    //유저 존재 확인
    public Optional<User> authenticate(String account, String password) {
        Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.checkPassword(password)) {
                return Optional.of(user); // 인증 성공
            }
        }
        return Optional.empty();
    }
}
