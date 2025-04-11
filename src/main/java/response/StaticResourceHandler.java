package response;

import request.RequestHeader;

import java.io.IOException;

public class StaticResourceHandler {
    public void sendResponse(RequestHeader requestHeader, ResponseBuilder responseBuilder) throws IOException {
        responseBuilder.sendStatic(requestHeader);
    }
}
