package webserver.http.response.handler;

import webserver.http.common.ContentType;
import webserver.http.common.StatusCode;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.ResponseBuilder;
import util.FileContentUtil;

import java.util.Optional;

public class StaticHandler implements Handler {
    @Override
    public Response handle(Request request) {
        ResponseBuilder responseBuilder;
        String path = request.getRequestLine("path");
        String extension = FileContentUtil.getExtension(path);

        Optional<byte[]> body = FileContentUtil.getFileContent(path);

        if (body.isEmpty()) {
            body = FileContentUtil.getFileContent("error/404.html");
            responseBuilder = new ResponseBuilder(StatusCode.NOT_FOUND, body.get(), ContentType.HTML.getContentType());
        } else {
            String contentType = ContentType.from(extension).getContentType();
            responseBuilder = new ResponseBuilder(StatusCode.OK, body.get(), contentType);
        }

        return responseBuilder.build();
    }
}
