package response.handler;

import common.ContentType;
import common.StatusCode;
import request.Request;
import response.Response;
import response.ResponseWriter;
import util.FileContentUtil;

import java.util.Optional;

public class StaticHandler implements Handler {
    @Override
    public Response handle(Request request) {
        ResponseWriter responseWriter;
        String path = request.getRequestLine("path");
        String extension = FileContentUtil.getExtension(path);

        Optional<byte[]> body = FileContentUtil.getFileContent(path);

        if (body.isEmpty()) {
            body = FileContentUtil.getFileContent("error/404.html");
            responseWriter = new ResponseWriter(StatusCode.NOT_FOUND, body.get(), ContentType.HTML.getContentType());
        } else {
            String contentType = ContentType.from(extension).getContentType();
            responseWriter = new ResponseWriter(StatusCode.OK, body.get(), contentType);
        }

        return responseWriter.write();
    }
}
