package org.apache.coyote.http11;

import nextstep.jwp.Response.Response;
import nextstep.jwp.Response.ResponseBody;
import nextstep.jwp.Response.ResponseHeader;
import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.request.Request;
import nextstep.jwp.util.ParsingUtil;
import nextstep.jwp.util.ResourceFinder;
import org.apache.coyote.Processor;
import org.apache.util.HttpResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.Socket;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    private final ControllerMapper controllerMapper = new ControllerMapper();
    private final ResourceFinder resourceFinder = new ResourceFinder();
    private final ParsingUtil parsingUtil = new ParsingUtil();

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

            Request request = parsingUtil.parseRequest(inputStream);

            Response response;

            if(request.getRequestHeader().getPath().contains(".")) {
                ResourceFinder resourceFinder = new ResourceFinder();
                ResponseBody responseBody = new ResponseBody(resourceFinder.getResource(request.getRequestHeader().getPath()));
                ResponseHeader responseHeader = new ResponseHeader(HttpResponseCode.OK.toString(),
                        HttpResponseCode.OK.getReasonPhrase(),
                        resourceFinder.getContentType(resourceFinder.getFileExtension(request.getRequestHeader().getPath())),
                        responseBody.getLength());
                log.info("Resource not found: " + responseBody.toString());
                response = new Response(responseHeader, responseBody);
            } else {

                response = controllerMapper.findController(request);
            }

            outputStream.write(response.toString().getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
}