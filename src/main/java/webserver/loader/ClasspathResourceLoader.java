package webserver.loader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClasspathResourceLoader implements ResourceLoader{
    private static final String PREFIX = "static";
    private static final String FILE_NOT_FOUND = "파일을 찾을 수 없습니다.";
    @Override
    public byte[] fileToBytes(String requestUrl) {
        try (InputStream is = getInputStreamByUrl(requestUrl)) {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = is.read(data, OFFSET, data.length)) != -1) {
                buffer.write(data, OFFSET, bytesRead);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getInputStreamByUrl(String requestUrl) throws FileNotFoundException {
        ClassLoader classLoader = ClasspathResourceLoader.class.getClassLoader();

        // 디렉토리에 접근했다 생각하고 index.html을 찾음
        String indexPath = PREFIX + requestUrl;
        if (!requestUrl.endsWith("/")) {
            indexPath += "/";
        }
        indexPath += "index.html";

        InputStream inputStream = classLoader.getResourceAsStream(indexPath);

        // index.html이 없으면, 파일 직접 요청
        if (inputStream == null) {
            String directPath = PREFIX + requestUrl;
            inputStream = classLoader.getResourceAsStream(directPath);
        }

        // 그래도 파일이 없으면 에러
        if (inputStream == null) {
            throw new FileNotFoundException(FILE_NOT_FOUND + " : " + requestUrl);
        }
        return inputStream;
    }
}
