package handler;

import exception.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponseHelper {

    private static final Logger log = LoggerFactory.getLogger(HttpResponseHelper.class);

    public static void sendResponse(OutputStream out, int statusCode,
                                    String statusText, String contentType, byte[] body) {
        try{
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("Connection: close\r\n");
            dos.writeBytes("\r\n");
            dos.write(body);
            dos.flush();
        } catch (IOException e){
            log.info("IOException Occur");
        }

    }

    public static void sendErrorResponse(OutputStream out, HttpException e) {
        String htmlBody = String.format(
                "<!DOCTYPE html><html><head><title>%d %s</title></head>" +
                        "<body><h1>%d %s</h1></body></html>",
                e.getStatusCode(), e.getMessage(),e.getStatusCode(), e.getMessage());
        byte[] body = htmlBody.getBytes(StandardCharsets.UTF_8);
        sendResponse(out, e.getStatusCode(), e.getMessage(), "text/html;charset=utf-8", body);
    }

    public static void sendRedirect(OutputStream out, String location) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("Connection: close\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.info("IOException Occur");
        }
    }
}
