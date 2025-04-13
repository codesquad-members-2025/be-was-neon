package handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponseHelper {

    public static void sendResponse(OutputStream out, int statusCode,
                                    String statusText, String contentType, byte[] body) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("Connection: close\r\n");
        dos.writeBytes("\r\n");
        dos.write(body);
        dos.flush();
    }

    public static void sendErrorResponse(OutputStream out, int statusCode, String statusText) throws IOException {
        String htmlBody = String.format(
                "<!DOCTYPE html><html><head><title>%d %s</title></head>" +
                        "<body><h1>%d %s</h1></body></html>",
                statusCode, statusText, statusCode, statusText);
        byte[] body = htmlBody.getBytes(StandardCharsets.UTF_8);
        sendResponse(out, statusCode, statusText, "text/html;charset=utf-8", body);
    }
}
