package response.handler;

import request.Request;
import response.Response;

import java.io.IOException;

public interface Handler {
    Response handle(Request request) throws IOException;
}
