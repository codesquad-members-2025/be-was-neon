package controller.handler;

import controller.UserController;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class RequestHandlerV2 implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String STATIC_DIRECTORY = "src/main/resources/static";
    private final Socket connection;

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

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                sendErrorResponse(out, 400, "Bad Request");
                return;
            }

            String method = requestParts[0];
            String fullPath = requestParts[1];
            logger.debug("Request: {} {}", method, fullPath);

            // 2. 경로와 쿼리스트링 분리
            String[] pathAndQuery = fullPath.split("\\?", 2);
            String path = pathAndQuery[0];
            String queryString = (pathAndQuery.length > 1) ? pathAndQuery[1] : null;

            // 3. 라우팅 처리
            if ("/create".equals(path) && "GET".equalsIgnoreCase(method)) {
                handleCreateUserRequest(queryString, out);
            } else {
                handleStaticRequest(path, out);
            }

        } catch (IOException e) {
            logger.error("Error handling request: {}", e.getMessage());
        }
    }

    private void handleCreateUserRequest(String queryString, OutputStream out) throws IOException {
        if (queryString == null || queryString.isEmpty()) {
            sendErrorResponse(out, 400, "Missing Parameters");
            return;
        }

        try {
            User createdUser = new UserController().createUser(queryString, out);
            String jsonResponse = convertUserToJson(createdUser); // JSON 변환
            sendResponse(out, 200, "OK", "application/json", jsonResponse.getBytes());
        } catch (Exception e) {
            logger.error("User creation failed: {}", e.getMessage());
            sendErrorResponse(out, 500, "Internal Server Error");
        }
    }

    private String convertUserToJson(User user) {
        return String.format(
                "{\"userId\":\"%s\",\"name\":\"%s\",\"email\":\"%s\"}",
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }

    private void handleStaticRequest(String path, OutputStream out) throws IOException {
        // 디렉토리 접근 시 index.html 리다이렉트
        if (path.endsWith("/")) {
            path += "index.html";
        } else if ("/".equals(path)) {
            path = "/index.html";
        }

        // URL 디코딩
        path = URLDecoder.decode(path, StandardCharsets.UTF_8.name());

        File file = new File(STATIC_DIRECTORY + File.separator + path.replace("/", File.separator));

        try {
            // 보안 검증
            validateFilePath(file);

            if (file.exists() && !file.isDirectory()) {
                serveFile(file, out);
            } else if (file.isDirectory()) {
                handleDirectoryRequest(file, out);
            } else {
                sendErrorResponse(out, 404, "Not Found");
            }
        } catch (SecurityException e) {
            sendErrorResponse(out, 403, "Forbidden");
        }
    }

    private void validateFilePath(File file) throws IOException, SecurityException {
        String canonicalPath = file.getCanonicalPath();
        String staticDirCanonical = new File(STATIC_DIRECTORY).getCanonicalPath();

        if (!canonicalPath.startsWith(staticDirCanonical)) {
            throw new SecurityException("Invalid file access attempt");
        }
    }

    private void handleDirectoryRequest(File directory, OutputStream out) throws IOException {
        File indexFile = new File(directory, "index.html");
        if (indexFile.exists()) {
            serveFile(indexFile, out);
        } else {
            sendErrorResponse(out, 403, "Directory Listing Denied");
        }
    }

    private void serveFile(File file, OutputStream out) throws IOException {
        byte[] body = readFileToByteArray(file);
        String contentType = determineContentType(file);
        sendResponse(out, 200, "OK", contentType, body);
        logger.info("Served file: {}", file.getPath());
    }

    private byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length())) {

            byte[] buffer = new byte[8192]; // 더 큰 버퍼 크기로 성능 향상
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

    private void sendErrorResponse(OutputStream out, int statusCode, String statusText) throws IOException {
        String htmlBody = String.format(
                "<!DOCTYPE html><html><head><title>%d %s</title></head>" +
                        "<body><h1>%d %s</h1></body></html>",
                statusCode, statusText, statusCode, statusText);
        byte[] body = htmlBody.getBytes(StandardCharsets.UTF_8);
        sendResponse(out, statusCode, statusText, "text/html;charset=utf-8", body);
    }

    private String extractRequestPath(String requestLine) {
        String[] parts = requestLine.split(" ");
        String path = (parts.length > 1) ? parts[1] : "/";

        // URL 파라미터 제거 (예: /path?query=value -> /path)
        int queryIndex = path.indexOf('?');
        if (queryIndex != -1) {
            path = path.substring(0, queryIndex);
        }

        return path;
    }

    private String determineContentType(File file) {
        String fileName = file.getName().toLowerCase();

        // HTML, CSS, JavaScript
        if (fileName.endsWith(".html")) return "text/html;charset=utf-8";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js")) return "application/javascript";

        // 이미지 파일
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".svg")) return "image/svg+xml";
        if (fileName.endsWith(".gif")) return "image/gif";

        // 파비콘
        if (fileName.endsWith(".ico")) return "image/x-icon";

        // 폰트 파일
        if (fileName.endsWith(".woff")) return "font/woff";
        if (fileName.endsWith(".woff2")) return "font/woff2";
        if (fileName.endsWith(".ttf")) return "font/ttf";
        if (fileName.endsWith(".eot")) return "application/vnd.ms-fontobject";
        if (fileName.endsWith(".otf")) return "font/otf";

        // 기타 파일
        return "application/octet-stream";
    }

    private void sendResponse(OutputStream out, int statusCode,
                              String statusText, String contentType, byte[] body) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("Connection: close\r\n");
        dos.writeBytes("Server: SimpleWebServer/1.0\r\n");
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
