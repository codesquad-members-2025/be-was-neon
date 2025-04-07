package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            Map<String, String> headers = parseRequestHeader(br);
            String requestLine = headers.get("Request-Line");
            if (requestLine == null || requestLine.isEmpty()) {
                send400Response(dos);
                return;
            }

            String[] tokens = requestLine.split(" ");
            if (tokens.length < 2) {
                send400Response(dos);
                return;
            }

            String uri = tokens[1];
            String resourcePath = "static" + uri;
            InputStream fileIn = null;

            // 먼저 URL을 통해 리소스 존재 여부 확인
            var resourceUrl = getClass().getClassLoader().getResource(resourcePath);
            if (resourceUrl != null) {
                // 파일 시스템 프로토콜이면 실제 디렉토리인지 확인
                if ("file".equals(resourceUrl.getProtocol())) {
                    File file = new File(resourceUrl.toURI());
                    if (file.isDirectory()) {
                        // 디렉토리인 경우, 자동으로 index.html을 붙여서 다시 시도
                        if (!uri.endsWith("/")) {
                            uri += "/";
                        }
                        resourcePath = "static" + uri + "index.html";
                        resourceUrl = getClass().getClassLoader().getResource(resourcePath);
                        if (resourceUrl != null) {
                            fileIn = resourceUrl.openStream();
                            uri = uri + "index.html";
                        }
                    } else {
                        fileIn = resourceUrl.openStream();
                    }
                } else {
                    // URI가 "/"로 끝나면 index.html 시도
                    if (uri.endsWith("/")) {
                        resourcePath = "static" + uri + "index.html";
                        resourceUrl = getClass().getClassLoader().getResource(resourcePath);
                        if (resourceUrl != null) {
                            fileIn = resourceUrl.openStream();
                            uri = uri + "index.html";
                        }
                    } else {
                        fileIn = resourceUrl.openStream();
                    }
                }
            }

            if (fileIn != null) {
                // 파일을 읽어 응답 본문에 담음
                byte[] body = fileIn.readAllBytes();
                String contentType = getContentType(uri);
                response200Header(dos, body.length, contentType);
                responseBody(dos, body);
            } else {
                logger.error("File not found: static{}", uri);
                String notFoundHtml = "<h1>404 Not Found</h1>";
                byte[] notFoundBody = notFoundHtml.getBytes();
                response404Header(dos, notFoundBody.length, "text/html;charset=utf-8");
                responseBody(dos, notFoundBody);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> parseRequestHeader(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();

        // 요청 라인(예: "GET / HTTP/1.1")을 읽고 저장
        String requestLine = br.readLine();
        headers.put("Request-Line", requestLine);
        logger.debug(requestLine);

        String line;
        // 요청 헤더를 읽고 저장
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            logger.debug(line);
            int colonIndex = line.indexOf(":");
            if (colonIndex != -1) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                headers.put(key, value);
            }
        }

        return headers;
    }

    private String getContentType(String uri) {
        String lowerUri = uri.toLowerCase();

        if (lowerUri.endsWith(".html") || lowerUri.endsWith(".htm")) {
            return "text/html;charset=utf-8";
        } else if (lowerUri.endsWith(".css")) {
            return "text/css;charset=utf-8";
        } else if (lowerUri.endsWith(".js")) {
            return "application/javascript;charset=utf-8";
        } else if (lowerUri.endsWith(".png")) {
            return "image/png";
        } else if (lowerUri.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (lowerUri.endsWith(".ico")) {
            return "image/x-icon";
        } else if (lowerUri.endsWith(".jpg") || lowerUri.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerUri.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerUri.endsWith(".json")) {
            return "application/json;charset=utf-8";
        } else if (lowerUri.endsWith(".xml")) {
            return "application/xml;charset=utf-8";
        } else {
            return "application/octet-stream";
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void send400Response(DataOutputStream dos) {
        String badRequestHtml = "<h1>400 Bad Request</h1>";
        byte[] badRequestBody = badRequestHtml.getBytes();
        try {
            dos.writeBytes("HTTP/1.1 400 Bad Request\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + badRequestBody.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(badRequestBody);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
