package webserver.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileResourceLoader implements ResourceLoader{
    private static final String PREFIX = "src/main/resources/static";

    @Override
    public InputStream getInputStreamByUrl(String requestUrl) throws FileNotFoundException {
        File file = new File(PREFIX + requestUrl);
        // 디렉토리면 index.html을 읽음
        if (file.isDirectory()) {
            file = new File(file, DEFAULT_PAGE);
        }

        // 파일 존재 여부 확인
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException(FILE_NOT_FOUND + file.getPath());
        }
        return new FileInputStream(file);
    }
}
