package org.apache.coyote.http11;

import static java.net.HttpURLConnection.HTTP_OK;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;
import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        try (final var inputStream = connection.getInputStream(); final var outputStream = connection.getOutputStream()) {

            final InputStream bufferedInputStream = new BufferedInputStream(inputStream);
            final InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
            final BufferedReader bf = new BufferedReader(inputStreamReader);
            String header = bf.readLine();
            ResponseDto response;
            if (header.split(" ")[1].contains("/assets/img")) {
                response = handleImageRequest(header);

            } else {
                response = handleRequest(header);
            }
            outputStream.write(response.getHeader());
            outputStream.write(response.getData());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
    private ResponseDto handleImageRequest(String requestSourceHeader) throws IOException {
        String[] info = requestSourceHeader.split(" ");
        URL resource = getClass().getClassLoader().getResource("static" + info[1]);
        final Path path = new File(resource.getFile()).toPath();
        byte[] filesIO = Files.readAllBytes(path);
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(obj)) {
            gzip.write(filesIO);
            gzip.flush();
        }
        byte[] compressedData = obj.toByteArray();
        int contentLength = compressedData.length;
        String header = String.join("\r\n",
                "HTTP/1.1 " + HTTP_OK + " " + "OK",
                "Content-Type: image/svg+xml",
                "Content-Encoding: gzip",
                "Content-Length: " + contentLength,
                "",
                "");
        return new ResponseDto(header,compressedData);
    }
    public ResponseDto handleRequest(String requestSourceHeader) throws IOException {
        log.info("request url : {}",requestSourceHeader);
        String[] info = requestSourceHeader.split(" "); // GET /login?account=gugu&password=pass HTTP/1.1
        final String RequestMethod = info[0];
        final String requestSourcePath = info[1];

        if (requestSourcePath.contains("/login?")) {
//            return info[0] + info[1];
        }
        return responseBuilderStaticPage(requestSourcePath);
    }
    public ResponseDto responseBuilderStaticPage(String requestSourcePath) throws IOException {
        String contentTypeFile = "text/html";
        Integer responseHeaderCode = 200;
        String responseHeaderMessage = "OK";
        if (requestSourcePath.equals("/")) {
            requestSourcePath ="/Home.txt";
        }
        else if(requestSourcePath.contains(".html") || requestSourcePath.contains("/js/scripts.js") || requestSourcePath.contains("/assets")||requestSourcePath.contains(".css")) {
            String contentTypeFileForm = requestSourcePath.substring(requestSourcePath.lastIndexOf(".")+1);
            contentTypeFile ="text/"+contentTypeFileForm;
        }
        else if(requestSourcePath.equals("/register")||requestSourcePath.equals("/login")) {
            requestSourcePath =requestSourcePath+".html";
        }
        else {
            requestSourcePath ="/404.html";
            responseHeaderCode = 404;
            responseHeaderMessage ="NOT FOUND";
        }
        URL resource = getClass().getClassLoader().getResource("static"+requestSourcePath);
        final Path path = new File(resource.getFile()).toPath();
        byte[] filesIO = Files.readAllBytes(path);
        var contentLength = filesIO.length;
        String responseHeader =  String.join("\r\n",
                "HTTP/1.1 "+responseHeaderCode+" "+responseHeaderMessage+" ",
                "Content-Type: "+contentTypeFile+";charset=utf-8 ",
                "Content-Length: " +contentLength+ " ",
                "",
                "");
        return new ResponseDto(responseHeader,filesIO);
    }
}
