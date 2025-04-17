package response.handler;

import loader.ResourceData;
import loader.StaticResourceLoader;
import request.Request;
import response.ContentTypeMapper;
import response.Response;
import response.ResponseSender;
import response.Status;

import java.io.IOException;

import static constants.HttpHeaders.CONTENT_LENGTH;
import static constants.HttpHeaders.CONTENT_TYPE;

public class StaticResourceHandler implements Handler {
    @Override
    public void sendResponse(Request request, ResponseSender responseSender) throws IOException {
        StaticResourceLoader staticResourceLoader = new StaticResourceLoader(request.getRequestHeader().getPath());
        ResourceData resourceData = staticResourceLoader.loadResourceData();

        byte[] body = resourceData.getInputStream().readAllBytes();
        String contentType = ContentTypeMapper.getContentType(resourceData.getExtension());

        Response response = Response.builder()
                .httpVersion(request.getRequestHeader().getHttpVersion())
                .status(Status.OK)
                .header(CONTENT_TYPE, contentType)
                .header(CONTENT_LENGTH, String.valueOf(body.length))
                .body(body)
                .build();

        responseSender.sendResponse(response);
    }
}
