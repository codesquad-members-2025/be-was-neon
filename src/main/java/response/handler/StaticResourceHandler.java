package response.handler;

import loader.ResourceData;
import loader.StaticResourceLoader;
import request.Request;
import response.ContentTypeMapper;
import response.Response;
import response.ResponseSender;
import response.Status;

import static constants.HttpHeaders.CONTENT_LENGTH;
import static constants.HttpHeaders.CONTENT_TYPE;

public class StaticResourceHandler implements Handler {
    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        ResourceData resourceData = StaticResourceLoader.loadResourceData(request.getRequestHeader().getPath());

        byte[] body = resourceData.readAllBytes();
        String contentType = ContentTypeMapper.getContentType(resourceData.getExtension());

        Response response = Response.builder()
                .httpVersion(request.getRequestHeader().getHttpVersion())
                .status(Status.OK)
                .header(CONTENT_TYPE, contentType)
                .header(CONTENT_LENGTH, String.valueOf(body.length))
                .body(body)
                .build();

        responseSender.send(response);
    }
}
