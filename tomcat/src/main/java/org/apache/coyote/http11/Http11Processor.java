package org.apache.coyote.http11;

import nextstep.exception.NotFoundControllerException;
import nextstep.jwp.FrontController;
import nextstep.jwp.RequestMapping;
import nextstep.jwp.controller.Controller;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.directory.NoSuchAttributeException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    private final FrontController frontController;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
        frontController = new FrontController(new RequestMapping());
    }

    @Override
    public void run() {
        process(connection);
    }
    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            HttpRequest httpRequest = HttpRequest.parse(br); //HttpRequest 객체 생성

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            HttpResponse httpResponse = new HttpResponse(); // HttpResponse 객체 생성

            httpResponse = frontController.execute(httpRequest, httpResponse);
            response(httpResponse, bw);
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAttributeException e) {
            throw new RuntimeException(e);
        }finally {
            System.out.println();
        }
    }
    private void response(HttpResponse httpResponse, BufferedWriter bw) throws IOException {
        bw.write(httpResponse.getResponse());
        bw.flush();
    }
}
