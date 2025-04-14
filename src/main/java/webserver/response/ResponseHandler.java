package webserver.response;

import static webserver.common.Constants.REQUEST_URL;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ContentTypeResolver;

public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    public static void createResponse(Map<String, List<String>> requestMap, OutputStream out, Response response) {
        DataOutputStream dos = new DataOutputStream(out);
        String type = ContentTypeResolver.getContentType(requestMap.get(REQUEST_URL).getFirst());

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
