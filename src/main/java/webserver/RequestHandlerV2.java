package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandlerV2 implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String STATIC_DIRECTORY = "src/main/resources/static";
    private Socket connection;

    public RequestHandlerV2(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP: {}, Port: {}",
                connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {

            // 1. HTTP 요청 파싱
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String requestLine = reader.readLine();
            if (requestLine == null) return;

            String path = extractRequestPath(requestLine);

            // 2. 파일 시스템에서 정적 파일 찾기
            File file = new File(STATIC_DIRECTORY + path);
            byte[] body;
            String contentType;

            if (file.exists() && !file.isDirectory()) {
                try (FileInputStream fis = new FileInputStream(file);
                     ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                    body = bos.toByteArray();
                }
                contentType = determineContentType(file);
                sendResponse(out, 200, "OK", contentType, body);
            } else {
                body = "<h1>404 Not Found</h1>".getBytes();
                sendResponse(out, 404, "Not Found", "text/html", body);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String extractRequestPath(String requestLine) {
        String[] parts = requestLine.split(" ");
        return (parts.length > 1) ? parts[1] : "/";
    }

    private String determineContentType(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".html")) return "text/html";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js")) return "application/javascript";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
    }

    private void sendResponse(OutputStream out, int statusCode,
                              String statusText, String contentType, byte[] body) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
