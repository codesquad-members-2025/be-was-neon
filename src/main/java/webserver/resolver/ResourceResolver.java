package webserver.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;

public class ResourceResolver {

    private static final Logger logger = LoggerFactory.getLogger(ResourceResolver.class);
    private static final String BASE_PATH = "static";
    private final HttpRequest request;
    private final HttpResponse response;

    public ResourceResolver(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    public void resolve() throws IOException {
        String uri = request.getUri();

        InputStream fileIn = getClass().getClassLoader().getResourceAsStream(BASE_PATH + uri);
        if (fileIn != null) {
            byte[] body = fileIn.readAllBytes();
            String contentType = getContentType(uri);
            response.sendResponse(200, "OK", contentType, body);

        } else {
            logger.error("File not found: static{}", uri);
            response.send404();
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
