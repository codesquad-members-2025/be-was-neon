package response.handler;

import request.RequestHeader;
import response.ResponseBuilder;

import java.io.IOException;

public class StaticResourceHandler implements Handler {
    @Override
    public void sendResponse(RequestHeader requestHeader, ResponseBuilder responseBuilder) throws IOException {
        responseBuilder.sendStatic(requestHeader);
    }
}
