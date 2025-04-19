package Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.Response;
import response.ResponseSender;
import response.Status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static constants.HttpHeaders.*;

public class ErrorResponder {
    private static final Logger logger = LoggerFactory.getLogger(ErrorResponder.class);
    private static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";
    private static final String DEFAULT_CONTENT_TYPE = "text/plain";

    public static void send(HttpException e, ResponseSender responseSender) throws IOException {
        String httpVersion = e.getRequest()
                .map(req -> req.getRequestHeader().getHttpVersion())
                .orElse(DEFAULT_HTTP_VERSION);

        byte[] body = e.getMessage().getBytes(StandardCharsets.UTF_8);

        Response response = Response.builder()
                .httpVersion(httpVersion)
                .status(e.getStatus())
                .header(CONTENT_LENGTH, String.valueOf(body.length))
                .header(CONTENT_TYPE,DEFAULT_CONTENT_TYPE)
                .body(body)
                .build();

        responseSender.send(response);
    }

    public static void send(IOException e, ResponseSender responseSender) throws IOException {
        send(new HttpException(Status.INTERNAL_SERVER_ERROR, e.getMessage()), responseSender);
    }
}
