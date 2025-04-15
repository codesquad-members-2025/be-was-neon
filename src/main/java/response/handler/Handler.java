package response.handler;

import request.Request;
import response.ResponseBuilder;

import java.io.IOException;

public interface Handler {
    public void sendResponse(Request request, ResponseBuilder responseBuilder) throws IOException;
}
