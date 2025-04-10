package webserver.loader;

import static webserver.request.RequestParser.COLON;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ClasspathResourceLoader implements ResourceLoader{
    private static final String PREFIX = "static";
    private static final String SLASH = "/";

    @Override
    public InputStream getInputStreamByUrl(String requestUrl) throws FileNotFoundException {
        ClassLoader classLoader = ClasspathResourceLoader.class.getClassLoader();

        // 디렉토리에 접근했다 생각하고 index.html을 찾음
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

        // 그래도 파일이 없으면 에러
        if (inputStream == null) {
            throw new FileNotFoundException(FILE_NOT_FOUND + COLON + requestUrl);
        }
        return inputStream;
    }
}
