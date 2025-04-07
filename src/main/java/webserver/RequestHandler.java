package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

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
            HttpResponse response = new HttpResponse(dos);

            HttpRequest request;
            try {
                request = new HttpRequest(br);
            } catch (IOException e) {
                logger.error("Request parsing error: {}", e.getMessage());
                response.send400();
                return;
            }

            String uri = request.getUri();
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
                response.sendResponse(200, "OK", contentType, body);

            } else {
                logger.error("File not found: static{}", uri);
                response.send404();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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

}
