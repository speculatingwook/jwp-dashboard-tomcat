package nextstep.jwp.handler;

import static java.net.HttpURLConnection.HTTP_OK;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;
import nextstep.jwp.dto.ResponseDto;
import nextstep.jwp.controller.RequestController;
import org.apache.coyote.http11.Http11Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
    private final RequestController requestController;

    public RequestHandler() {
        requestController = new RequestController();
    }

    public ResponseDto getResponse(String header) throws IOException {
        ResponseDto response;
        String[] info = header.split(" ");
        final String requestMethod = info[0];
        final String requestSourcePath = info[1];
        if (requestSourcePath.contains("/login?") ||requestSourcePath.contains("/register?") ) {
            return requestController.handleMember(requestMethod,requestSourcePath);
        }
        if (requestMethod.equals("GET") && requestSourcePath.contains("/assets/img")) {
            return handleImageRequest(requestSourcePath);
        }
        return responseBuilderStaticPage(requestSourcePath);
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
