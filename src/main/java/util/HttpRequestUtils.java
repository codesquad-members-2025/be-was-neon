package util;

import java.io.*;

public class Util {

    public static String extractRequestUrl(String requestLine) {
        String[] requestLineArr = requestLine.split(" ");
        return requestLineArr[1];
    }

    public static byte[] readFileBytes(String relativePath) throws IOException {
        try (InputStream is = Util.class.getClassLoader().getResourceAsStream("static" + relativePath)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: static" + relativePath);
            }
            return is.readAllBytes();
        }
    }
}
