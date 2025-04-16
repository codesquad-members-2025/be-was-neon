package webserver.loader;

import static webserver.common.Constants.COLON;
import static webserver.common.Constants.SLASH;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ClasspathResourceLoader implements ResourceLoader{
    private static final String PREFIX = "static";

    @Override
    public InputStream getInputStreamByUrl(String requestUrl) throws FileNotFoundException {
        if (!exists(requestUrl)) {
            throw new FileNotFoundException(FILE_NOT_FOUND + COLON + requestUrl);
        }

        ClassLoader classLoader = ClasspathResourceLoader.class.getClassLoader();

        // 먼저 디렉토리로 간주하고 index.html을 시도
        String indexPath = PREFIX + requestUrl;
        if (!requestUrl.endsWith(SLASH)) {
            indexPath += SLASH;
        }
        indexPath += DEFAULT_PAGE;

        InputStream inputStream = classLoader.getResourceAsStream(indexPath);

        // index.html이 없으면, 파일 직접 요청
        if (inputStream == null) {
            String directPath = PREFIX + requestUrl;
            inputStream = classLoader.getResourceAsStream(directPath);
        }

        return inputStream;
    }

    @Override
    public boolean exists(String path) {
        String fullPath = PREFIX + path;
        if (!path.endsWith(SLASH)) {
            fullPath += SLASH;
        }
        String indexPath = fullPath + DEFAULT_PAGE;

        ClassLoader classLoader = ClasspathResourceLoader.class.getClassLoader();

        // index.html이 존재하는 경우
        if (classLoader.getResource(indexPath) != null) {
            return true;
        }

        // 파일 직접 요청
        String directPath = PREFIX + path;
        return classLoader.getResource(directPath) != null;
    }
}
