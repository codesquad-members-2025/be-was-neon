package loader;

import Exceptions.HttpException;
import response.Status;
import utils.FileExtentionExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static constants.SpecialChars.*;

public class StaticResourceLoader {
    private static final String BASE_PATH = "static";
    private static final String DEFAULT_INDEX_PATH = "index.html";

    public static ResourceData loadResourceData(String path) {
        try{
            URL resourceUrl = StaticResourceLoader.class.getClassLoader().getResource(BASE_PATH + path);

            // 해당 경로가 디렉토리거나, 찾을 수 없으면 해당 디렉토리의 index.html을 사용
            if (resourceUrl == null || !path.contains(DOT)) {
                String folderIndexPath = path.endsWith(SLASH) ? path + DEFAULT_INDEX_PATH : path + SLASH + DEFAULT_INDEX_PATH;
                resourceUrl = StaticResourceLoader.class.getClassLoader().getResource(BASE_PATH + folderIndexPath);
            }
            // 만약 해당 디렉토리에도 index.html이 없으면 기본 index.html 사용
            if (resourceUrl == null) {
                throw new HttpException(Status.NOT_FOUND, "Resource not found: " + path);
            }

            // 리소스 경로에서 확장자 추출
            String resourcePath = resourceUrl.getPath();
            String fileExtension = FileExtentionExtractor.getFileExtension(resourcePath);

            InputStream inputStream = resourceUrl.openStream();
            return new ResourceData(fileExtension, inputStream);
        } catch (IOException e) {
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, "Failed to load resource: " + path);
        }
    }
}
