package response.handler;

import request.RequestHeader;
import response.ResponseBuilder;

import java.io.IOException;

public interface Handler {
    public void sendResponse(RequestHeader requestHeader, ResponseBuilder responseBuilder) throws IOException;
}
