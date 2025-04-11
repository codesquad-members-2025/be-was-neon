package webserver.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.*;

public class FileResourceLoader implements ResourceLoader {

    private final String PREFIX = "src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(FileResourceLoader.class);

    @Override
    public byte[] fileToBytes(String requestURL) {
        File file = new File(PREFIX + requestURL);
        try (FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                out.write(buffer, OFFSET, read);
            }
            return out.toByteArray();
        } catch (IOException e) {
            logger.error("파일을 읽는 중 오류 발생, e");
            throw new RuntimeException(e);
        }
    }
}
