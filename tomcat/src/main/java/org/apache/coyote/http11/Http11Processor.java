package org.apache.coyote.http11;

import org.apache.coyote.Processor;
import org.apache.coyote.http11.exception.BadRequestException;
import org.apache.coyote.http11.handler.ExceptionHandler;
import org.apache.coyote.http11.handler.Handler;
import org.apache.coyote.http11.handler.HandlerAdapter;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream();
             final var reader = new InputStreamReader(inputStream);
             final var bufferedReader = new BufferedReader(reader)) {

            HttpResponse httpResponse;
            try {
                HttpRequest httpRequest = HttpRequest.parse(bufferedReader);
                System.out.println(httpRequest.getPath()); // TODO Delete

                Handler handler = HandlerAdapter.getHandler(httpRequest);
                if (handler == null) {
                    throw new BadRequestException("Handler not found");
                }
                httpResponse = handler.handle(httpRequest);
            } catch (Exception e) {
                log.error(e.getMessage());
                httpResponse = ExceptionHandler.handle(e);
            }

            outputStream.write(httpResponse.toString().getBytes());
            outputStream.flush();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
