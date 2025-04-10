package loader;

import utils.FileExtentionExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class StaticResourceLoader {
    private static final String BASE_PATH = "static/";
    private final String path;

    public StaticResourceLoader(String path) {
        this.path = path;
    }

    public ResourceData loadResourceData() throws IOException {
        URL resourceUrl = getClass().getClassLoader().getResource(BASE_PATH + path);

        if (resourceUrl == null) {
            throw new RuntimeException("리소스를 찾을 수 없습니다.");
        }

        // 리소스 경로에서 확장자 추출
        String resourcePath = resourceUrl.getPath();
        String fileExtension = FileExtentionExtractor.getFileExtension(resourcePath);


        InputStream inputStream = resourceUrl.openStream();
        return new ResourceData(fileExtension, inputStream);
    }
}
