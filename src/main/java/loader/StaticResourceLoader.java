package loader;

import java.io.IOException;
import java.io.InputStream;

public class StaticResourceLoader {
    private static final String BASE_PATH = "static/";

    public byte[] loadResourceAsBytes(String path) {
        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(BASE_PATH + path);
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
