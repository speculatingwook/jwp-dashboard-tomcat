package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.controller.Controller;
import org.apache.coyote.http11.controller.RequestMapping;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
        try{
            execute(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void execute(final Socket connection) throws IOException{
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream();) {
            final HttpRequest request = new HttpRequest(inputStream);
            final HttpResponse response = new HttpResponse(outputStream);

            Controller controller = RequestMapping.getController(request.getHttpRequestLine().getPath());
            controller.service(request, response);
            response.getResponse();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
}