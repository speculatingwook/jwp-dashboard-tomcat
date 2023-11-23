package org.apache.coyote.http11.Utill;

import org.apache.coyote.http11.exception.base.InternalServerException;
import org.apache.coyote.http11.exception.base.NotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileFinder {

    private final String staticAbsolutePath = "static";

    public String getFileToStringFromPath(String filePath) {
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

    public boolean isExist(String filePath) {
        try {
            URL resource = this.getClass()
                    .getClassLoader()
                    .getResource(staticAbsolutePath + filePath);
            Path path = Paths.get(resource.toURI());
            return Files.exists(path);
        } catch (NullPointerException | URISyntaxException e) {
            return false;
        }
    }
}
