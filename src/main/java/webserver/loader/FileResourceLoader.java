package webserver.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import webserver.exception.ResourceNotFoundException;

public class FileResourceLoader implements ResourceLoader{
    private static final String PREFIX = "src/main/resources/";

    @Override
    public InputStream getInputStreamByUrl(String requestUrl, boolean isTemplate) throws FileNotFoundException {
        String prefix = isTemplate ? PREFIX + TEMPLATE : PREFIX + STATIC;
        File file = new File(prefix + requestUrl);

        // 디렉토리인 경우 index.html로 경로 수정
        if (file.isDirectory()) {
            file = new File(file, DEFAULT_PAGE);
        }

        return new FileInputStream(file);
    }

    @Override
    public boolean exists(String path) {
        Path filePath = Paths.get(PREFIX + STATIC, path);
        if (Files.isDirectory(filePath)) {
            filePath = filePath.resolve(DEFAULT_PAGE);
        }
        if (!Files.exists(filePath)) throw new ResourceNotFoundException(FILE_NOT_FOUND);
        return true;
    }
}
