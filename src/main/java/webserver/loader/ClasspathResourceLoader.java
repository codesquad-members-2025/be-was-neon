package webserver.loader;

import static webserver.common.Constants.COLON;
import static webserver.common.Constants.SLASH;

import java.io.FileNotFoundException;
import java.io.InputStream;
import webserver.exception.ResourceNotFoundException;

public class ClasspathResourceLoader implements ResourceLoader{
    private static final String PREFIX = "static";

    @Override
    public InputStream getInputStreamByUrl(String requestUrl, boolean isTemplate) {
        String prefix = isTemplate ? TEMPLATE : STATIC;
        ClassLoader classLoader = ClasspathResourceLoader.class.getClassLoader();

        // 먼저 디렉토리로 간주하고 index.html을 시도
        String indexPath = prefix + requestUrl;
        if (!requestUrl.endsWith(SLASH)) {
            indexPath += SLASH;
        }
        indexPath += DEFAULT_PAGE;

        InputStream inputStream = classLoader.getResourceAsStream(indexPath);

        // index.html이 없으면, 파일 직접 요청
        if (inputStream == null) {
            String directPath = prefix + requestUrl;
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

        if (classLoader.getResource(indexPath) != null) {
            return true;
        }

        String directPath = PREFIX + path;
        if (classLoader.getResource(directPath) != null) {
            return true;
        }

        throw new ResourceNotFoundException(FILE_NOT_FOUND);
    }
}
