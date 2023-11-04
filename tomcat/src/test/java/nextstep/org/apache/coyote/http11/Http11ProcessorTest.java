package nextstep.org.apache.coyote.http11;

import org.junit.jupiter.api.DisplayName;
import support.StubSocket;
import org.apache.coyote.http11.Http11Processor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class Http11ProcessorTest {

//    @Test
//    void process() {
//        // given
//        final var socket = new StubSocket();
//        final var processor = new Http11Processor(socket);
//
//        // when
//        processor.process(socket);
//
//        // then
//        var expected = String.join("\r\n",
//                "HTTP/1.1 200 OK ",
//                "Content-Type: text/html;charset=utf-8 ",
//                "",
//                "Hello world!");
//
//        assertThat(socket.output()).isEqualTo(expected);
//    }

    @DisplayName("루트 주소로 접속시, index.html 파일을 불러온다.")
    @Test
    void rootPage() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET / HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        final URL resource = ClassLoader.getSystemResource("static/index.html");
        final Path path = Path.of(resource.toURI());

        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "\r\n" +
                new String(Files.readAllBytes(path));

        assertThat(socket.output()).isEqualTo(expected);
    }


    @DisplayName("/index.html 페이지 요청시 응답으로 반환된다.")
    @Test
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final Http11Processor processor = new Http11Processor(socket);

        // when
        processor.process(socket);

        // then
        final URL resource = ClassLoader.getSystemResource("static/index.html");
        final Path path = Path.of(resource.toURI());

        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "\r\n" +
                new String(Files.readAllBytes(path));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("CSS 파일 로드가 정상적으로 수행된다.")
    @Test
    void loadCss() {
        // given
        final String httpRequest= String.join("\r\n",
                "GET /css/styles.css HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Accept: text/css,*/*;q=0.1 ",
                "Connection: keep-alive ",
                "",
                "");
        final var socket = new StubSocket(httpRequest);
        final Http11Processor processor = new Http11Processor(socket);
        // when
        processor.process(socket);
        // then
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/css;charset=utf-8 \r\n";
        assertThat(socket.output()).startsWith(expected);
    }

    @DisplayName("JS 파일 로드가 정상적으로 수행된다.")
    @Test
    void loadJs() {
        // given
        final String httpRequest= String.join("\r\n",
                "GET /js/scripts.js HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Accept: */* ",
                "Connection: keep-alive ",
                "",
                "");
        final var socket = new StubSocket(httpRequest);
        final Http11Processor processor = new Http11Processor(socket);
        // when
        processor.process(socket);
        // then
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: application/javascript;charset=utf-8 \r\n";

        assertThat(socket.output()).startsWith(expected);
    }
}
