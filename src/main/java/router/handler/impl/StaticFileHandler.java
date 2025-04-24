package router.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.handler.HttpRequestHandler;
import util.FileUtils;
import webserver.common.ContentType;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class StaticFileHandler implements HttpRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticFileHandler.class);

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        if (path.equals("/") || path.equals("/?")) {
            path = "/index.html";
        }

        ContentType contentType = ContentType.getContentTypeByPath(path);

        try {
            byte[] body = FileUtils.readFileBytes(path);
            response.sendOk(contentType, body);
        } catch (IOException e) {
            logger.error("파일을 찾을 수 없습니다: {}", path);
            response.send404();
        }
    }
}
