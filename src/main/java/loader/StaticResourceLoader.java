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

    public ResourceData loadResourceData() {
        URL resourceUrl = getClass().getClassLoader().getResource(BASE_PATH + path);

        if (resourceUrl == null) {
            throw new RuntimeException("리소스를 찾을 수 없습니다.");
        }

        // 리소스 경로에서 확장자 추출
        String resourcePath = resourceUrl.getPath();
        String fileExtension = FileExtentionExtractor.getFileExtension(resourcePath);

        try {
            // InputStream으로 리소스 읽기
            InputStream inputStream = resourceUrl.openStream();
            return new ResourceData(fileExtension, inputStream);
        } catch (IOException e) {
            throw new RuntimeException("리소스를 읽는 데 실패했습니다.", e);
        }
    }
}
