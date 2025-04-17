package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.resolver.ContentTypeResolver;
import webserver.request.Request;

public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    public static void createResponse(Request request, OutputStream out, Response response) {
        DataOutputStream dos = new DataOutputStream(out);
        String type = ContentTypeResolver.getContentType(request.getRequestUrl());

        String header = ResponseHeaderFactory.createHeader(type, response).toString();
        sendResponse(dos, response.getBody(), header);
    }
    private static void sendResponse(DataOutputStream dos, byte[] body, String header) {
        try {
            dos.writeBytes(header);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
