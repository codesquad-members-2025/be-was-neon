package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class FileContentUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileContentUtil.class);

    public static String getFileExtension(String url) {
        String[] tokens = url.split("\\.");
        return tokens[1];
    }

    public static byte[] getFileContent(String path) {
        byte[] body = new byte[0];

        try (FileInputStream fis = new FileInputStream("src/main/resources/static/" + path)) {
            body = fis.readAllBytes();

        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage());
        }

        return body;
    }

}
