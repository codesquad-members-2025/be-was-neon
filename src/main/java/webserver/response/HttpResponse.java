package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ContentType;
import utils.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    public static final String CRLF = "\r\n";
    public static final String HTTP_1_1 = "HTTP/1.1";

    private final DataOutputStream dos;
    private HttpStatus status;
    private ContentType contentType;
    private final Map<String, List<String>> headers = new HashMap<>();
    private byte[] body;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes(HTTP_1_1 + " 200 OK " + CRLF);
            dos.writeBytes("Content-Type: text/html;charset=utf-8" + CRLF);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + CRLF);
            dos.writeBytes(CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public HttpResponse status(HttpStatus status) {
        this.status = status;
        return this;
    }

    public HttpResponse contentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public HttpResponse body(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpResponse headers(String name, String value) {
        headers.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
        return this;
    }
}
