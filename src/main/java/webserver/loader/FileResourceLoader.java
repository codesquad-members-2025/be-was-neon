package webserver.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class FileResourceLoader implements ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(FileResourceLoader.class);
    private final String PREFIX = "src/main/resources/static";
    private final List<String> RESOURCE_LIST = List.of("/article", "/comment", "/login", "/registration");

    @Override
    public byte[] fileToBytes(String requestURL) {
        requestURL = findResourcePath(requestURL);
        File file = new File(PREFIX + requestURL);  // isDirectory
        try (FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                out.write(buffer, OFFSET, read);
            }
            return out.toByteArray();
        } catch (IOException e) {
            logger.error("파일을 읽는 중 오류 발생", e);
            throw new RuntimeException(e);
        }
    }

    private String findResourcePath(String requestURL) {
        for(String resource : RESOURCE_LIST) {
            if(requestURL.startsWith(resource)) {
                return resource + "/index.html";
            }
        }
        return requestURL;
    }
}
