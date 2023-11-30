package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.HTTPRequest.HttpRequest;
import org.apache.coyote.http11.HTTPResponse.HttpResponse;
import org.apache.coyote.http11.controller.Controller;
import org.apache.coyote.http11.controller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    private final FrontController CONTROLLER;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
        this.CONTROLLER = new FrontController();
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

            HttpRequest httpRequest = new HttpRequest(reader);

            Controller controller =  CONTROLLER.match(httpRequest);
            HttpResponse httpResponse = controller.handleRequest(httpRequest);

            outputStream.write(httpResponse.generateResponse().getBytes());
            outputStream.flush();

        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

}
