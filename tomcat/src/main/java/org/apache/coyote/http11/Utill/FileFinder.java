package org.apache.coyote.http11.Utill;

import org.apache.coyote.http11.exception.InternalServerException;
import org.apache.coyote.http11.exception.NotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileFinder {

    private final String staticAbsolutePath = "static";

    public String fromPath(String filePath) {
        try {
            URL resource = this.getClass()
                    .getClassLoader()
                    .getResource(staticAbsolutePath + filePath);
            Path path = Paths.get(resource.toURI());
            return new String(Files.readAllBytes(path));
        } catch (NullPointerException | URISyntaxException e) {
            throw new NotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
