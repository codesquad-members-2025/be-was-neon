package response.handler;

import Exceptions.HttpException;
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
    public void sendResponse(Request request, ResponseSender responseSender){
        try {
            ResourceData resourceData = StaticResourceLoader.loadResourceData(request.getRequestHeader().getPath());

            byte[] body = resourceData.getInputStream().readAllBytes();
            String contentType = ContentTypeMapper.getContentType(resourceData.getExtension());

            Response response = Response.builder()
                    .httpVersion(request.getRequestHeader().getHttpVersion())
                    .status(Status.OK)
                    .header(CONTENT_TYPE, contentType)
                    .header(CONTENT_LENGTH, String.valueOf(body.length))
                    .body(body)
                    .build();

            responseSender.send(response);
        } catch (IOException ex) {
            throw new HttpException(Status.NOT_FOUND, request, ex.getMessage());
        }
    }
}
