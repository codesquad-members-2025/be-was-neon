package loader;

import utils.FileExtentionExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class StaticResourceLoader {
    private static final String BASE_PATH = "static/";
    private static final String DEFAULT_INDEX_PATH = "index.html";
    private final String path;

    public StaticResourceLoader(String path) {
        this.path = path;
    }

    public ResourceData loadResourceData() throws IOException {
        URL resourceUrl = getClass().getClassLoader().getResource(BASE_PATH + path);

        // 해당 경로가 디렉토리거나, 찾을 수 없으면 해당 디렉토리의 index.html을 사용
        if (resourceUrl == null || !path.contains(".")) {
            String folderIndexPath = path.endsWith("/") ? path + "index.html" : path + "/index.html";
            resourceUrl = getClass().getClassLoader().getResource(BASE_PATH + folderIndexPath);
        }
        // 만약 해당 디렉토리에도 index.html이 없으면 기본 index.html 사용
        if (resourceUrl == null) {
            resourceUrl = getClass().getClassLoader().getResource(BASE_PATH + DEFAULT_INDEX_PATH);
        }

        // 리소스 경로에서 확장자 추출
        String resourcePath = resourceUrl.getPath();
        String fileExtension = FileExtentionExtractor.getFileExtension(resourcePath);

        InputStream inputStream = resourceUrl.openStream();
        return new ResourceData(fileExtension, inputStream);
    }
}
