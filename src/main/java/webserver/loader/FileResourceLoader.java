package webserver.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileResourceLoader implements ResourceLoader{
    private static final String PREFIX = "src/main/resources/static";

    @Override
    public InputStream getInputStreamByUrl(String requestUrl) throws FileNotFoundException {
        File file = new File(PREFIX + requestUrl);

        // 디렉토리인 경우 index.html로 경로 수정
        if (file.isDirectory()) {
            file = new File(file, DEFAULT_PAGE);
        }

        return new FileInputStream(file);
    }

    @Override
    public boolean exists(String path) {
        Path filePath = Paths.get(PREFIX, path);
        if (Files.isDirectory(filePath)) {
            filePath = filePath.resolve(DEFAULT_PAGE);
        }
        return Files.exists(filePath);
    }
}
