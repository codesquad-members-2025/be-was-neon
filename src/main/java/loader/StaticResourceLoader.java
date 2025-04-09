package loader;

import java.io.IOException;
import java.io.InputStream;

public class StaticResourceLoader implements ResourceLoader {
    private static final String BASE_PATH = "static/";

    @Override
    public byte[] loadResourceAsBytes(String path) {
        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(BASE_PATH + path);
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
