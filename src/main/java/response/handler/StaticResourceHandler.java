package response.handler;

import request.Request;
import response.ResponseBuilder;

import java.io.IOException;

public class StaticResourceHandler implements Handler {
    @Override
    public void sendResponse(Request request, ResponseBuilder responseBuilder) throws IOException {
        responseBuilder.sendStatic(request.getRequestHeader());
    }
}
