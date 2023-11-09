package nextstep.jwp.controller;

import nextstep.jwp.Response.ResponseBody;
import nextstep.jwp.Response.ResponseHeader;
import nextstep.jwp.service.LoginService;
import nextstep.jwp.util.ResourceFinder;
import nextstep.jwp.Response.Response;
import org.apache.util.HttpResponseCode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    private LoginService loginService = new LoginService();
    private ResourceFinder resourceFinder = new ResourceFinder();
    private ResponseHeader responseHeader = new ResponseHeader();
    private ResponseBody responseBody = new ResponseBody();

    // url: /login
    public Response handleRequest(String method, String url) throws IOException {
        if(url.startsWith("/login?")) {
            // url: /login?account=gugu&password=password
            String query = url.substring("/login?".length());

            Map<String, String> queryParameters = parseQueryParameters(query);

            if(loginService.login(queryParameters.get("account"), queryParameters.get("password"))) {
                // login 성공
                responseBody.setFileContent(resourceFinder.getResource("/index.html")); // todo: 리다이렉션으로 바꿀 것

                responseHeader.setResponseCode(HttpResponseCode.FOUND.toString());
                responseHeader.setResponseStatus(HttpResponseCode.FOUND.getReasonPhrase());
                responseHeader.setContentType(resourceFinder.getContentType(resourceFinder.getFileExtension("/index.html")));
                responseHeader.setContentLength(responseBody.getLength());
                responseHeader.setLocation("/index.html");
            } else {
                // login 실패
                responseBody.setFileContent(resourceFinder.getResource("/401.html")); // todo: 리다이렉션으로 바꿀 것

                responseHeader.setResponseCode(HttpResponseCode.UNAUTHORIZED.toString());
                responseHeader.setResponseStatus(HttpResponseCode.UNAUTHORIZED.getReasonPhrase());
                responseHeader.setContentType(resourceFinder.getContentType(resourceFinder.getFileExtension("/401.html")));
                responseHeader.setContentLength(responseBody.getLength());
                responseHeader.setLocation("/404.html");

            }

            return new Response(responseHeader, responseBody);

        } else {
            // url: /login
            responseBody.setFileContent(resourceFinder.getResource(url + ".html"));

            responseHeader.setResponseCode(HttpResponseCode.OK.toString());
            responseHeader.setResponseStatus(HttpResponseCode.OK.getReasonPhrase());
            responseHeader.setContentType(resourceFinder.getContentType(resourceFinder.getFileExtension(url + ".html")));
            responseHeader.setContentLength(responseBody.getLength());

            return new Response(responseHeader, responseBody);
        }
    }

    public Map<String, String> parseQueryParameters(String query) throws UnsupportedEncodingException {
        String[] params = query.split("&");

        Map<String, String> queryParameters = new HashMap<>();

        for (String param : params) {
            String[] keyValue = param.split("=");

            if (keyValue.length == 2) {
                String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                queryParameters.put(key, value);
            }
        }
        return queryParameters;
    }
}
