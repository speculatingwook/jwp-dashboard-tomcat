package org.apache.coyote.http11.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class LoginController implements Controller {

    User user;
    @Override
    public void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Map<String, String> params = httpRequest.getParams();
        String path = httpRequest.getRequestPath();
        if (!params.isEmpty()){
            System.out.println(params.size());
            handleLoginRequest(httpRequest, httpResponse);
        } else {
            File resourceFile = new File("/Users/hayoon/spring/jwp-dashboard-http-mission/tomcat/src/main/resources/static" + path + ".html");
            if (resourceFile.exists()) {
                byte[] content = Files.readAllBytes(Path.of(resourceFile.getAbsolutePath()));
                String responseBody = new String(content);
                System.out.println(responseBody);
                httpResponse.setResponseBody(responseBody);
                httpResponse.setStatusCode(200);
            } else {
                String responseBody = "Resource not found.";
                httpResponse.setResponseBody(responseBody);
                httpResponse.setStatusCode(404); // HTTP 상태코드 404 (Not Found)
            }
        }
    }
    private void handleLoginRequest(HttpRequest httpRequest, HttpResponse httpResponse) {
        String account = httpRequest.getParams().get("account");
        String password = httpRequest.getParams().get("password");

        Optional<User> loginUser = authenticate(account, password);

        if (loginUser.isPresent()) {
            user = loginUser.orElse(null);
            String responseBody = "Login successful! : " + user.toString();
            httpResponse.setResponseBody(responseBody);
            httpResponse.setStatusCode(200); // HTTP 상태코드 200 (OK)
        } else {
            String responseBody = "Authentication failed. Please check your credentials.";
            httpResponse.setResponseBody(responseBody);
            httpResponse.setStatusCode(401); // HTTP 상태코드 401 (Unauthorized)
        }
    }
    private Optional<User> authenticate(String account, String password) {
        Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.checkPassword(password)) {
                return Optional.of(user); // 인증 성공
            }
        }
        return Optional.empty(); // 인증 실패
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
}
