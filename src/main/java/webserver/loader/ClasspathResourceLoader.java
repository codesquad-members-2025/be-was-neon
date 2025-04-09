package webserver.loader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import webserver.request.RequestHandler;

public class ClasspathResourceLoader implements ResourceLoader{
    private static final String PREFIX = "./static";
    private static final String FILE_NOT_FOUND = "파일을 찾을 수 없습니다.";
    @Override
    public byte[] fileToBytes(String requestUrl) {
        ClassLoader classLoader = RequestHandler.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(PREFIX + requestUrl)) {
            if (inputStream == null) {
                throw new FileNotFoundException(FILE_NOT_FOUND);
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(data, OFFSET, data.length)) != -1) {
                buffer.write(data, OFFSET, bytesRead);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
