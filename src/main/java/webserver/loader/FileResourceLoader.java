package webserver.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class FileResourceLoader implements ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(FileResourceLoader.class);
    private final String PREFIX = "src/main/resources/static";

    @Override
    public FileResult fileToBytes(String requestURL) {
        File file = new File(PREFIX + requestURL);
        if(file.isDirectory()) {
            file = new File(file, "index.html");
            requestURL += "/index.html";
        }

        try (FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                out.write(buffer, OFFSET, read);
            }
            return new FileResult(out.toByteArray(), requestURL);
        } catch (IOException e) {
            logger.error("파일을 읽는 중 오류 발생", e);
            throw new RuntimeException(e);
        }
    }
}
