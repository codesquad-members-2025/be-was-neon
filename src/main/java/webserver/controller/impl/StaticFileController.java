package webserver.controller.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ContentType;
import utils.FileUtils;
import webserver.controller.Controller;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public class StaticFileController implements Controller{
    private static final Logger logger = LoggerFactory.getLogger(StaticFileController.class);
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException{
        String path = request.getPath();

        ContentType contentType = ContentType.getContentTypeByPath(path);
        try {
            byte[] body = FileUtils.readFileBytes(path);
            response.sendOk(contentType, body);
        } catch (IOException e) {
            logger.error("STATIC FILE NOT FOUND: {}", path);
            response.send404();
        }
    }
}
