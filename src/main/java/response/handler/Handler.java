package response.handler;

import request.Request;
import response.ResponseSender;

import java.io.IOException;

public interface Handler {
    public void sendResponse(Request request, ResponseSender responseSender) throws IOException;
}
