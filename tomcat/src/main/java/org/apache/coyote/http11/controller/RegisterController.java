package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Service.RegisterService;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.request.HttpRequestBody;
import org.apache.coyote.http11.request.HttpRequestHeader;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.HttpMethod;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpStatusCode;
import org.apache.coyote.http11.util.ParseUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

public class RegisterController implements Controller{
    private static final String INDEX_PAGE_URL = "index.html";
    private static final String REGISTER_PAGE_URL = "static/register.html";
    private static final String NOT_FOUND_PAGE_URL = "static/404.html";

    private final RegisterService  registerService = new RegisterService();
    private Router router;

    public RegisterController() {
        router = new Router();
        router.addHandler(HttpMethod.GET, "/register", this::handleGetRegister);
        router.addHandler(HttpMethod.POST, "/register", this::handlePostRegister);
    }

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        return router.mappingEndpointHandler(httpRequest);
    }

    private HttpResponse handleGetRegister(HttpRequest httpRequest) throws IOException {
        HttpRequestHeader httpRequestHeader = httpRequest.getHttpRequestHeader();
        String requestUri = httpRequestHeader.getPath();

        URL resource = getClass()
                .getClassLoader()
                .getResource(REGISTER_PAGE_URL);

        File file = new File(resource.getFile());

        String responseBody = new String(Files.readAllBytes(file.toPath()));

        return HttpResponse.builder()
                .statusCode(HttpStatusCode.OK.getCode())
                .statusMessage(HttpStatusCode.OK.getMessage())
                .addHeader("Content-Type", ContentType.findContentTypeFromUri(requestUri).getContentType())
                .addHeader("Content-Length", String.valueOf(responseBody.getBytes().length))
                .body(responseBody)
                .build();
    }

    private HttpResponse handlePostRegister(HttpRequest httpRequest) throws UnsupportedEncodingException {
        HttpRequestBody httpRequestBody = httpRequest.getHttpRequestBody();

        String requestBody = httpRequestBody.getBody();
        Map<String, String> queryParameters = ParseUtil.parseQueryParameters(requestBody);
        String account = queryParameters.get("account");
        String password = queryParameters.get("password");
        String email = queryParameters.get("email");

        if(registerService.register(account, password, email)) {
            return HttpResponse.builder()
                    .statusCode(HttpStatusCode.FOUND.getCode())
                    .statusMessage(HttpStatusCode.FOUND.getMessage())
                    .addHeader("Location", INDEX_PAGE_URL)
                    .build();
        }

        return HttpResponse.builder()
                .statusCode(HttpStatusCode.NOT_FOUND.getCode())
                .statusMessage(HttpStatusCode.NOT_FOUND.getMessage())
                .addHeader("Location", NOT_FOUND_PAGE_URL)
                .build();
    }

}


