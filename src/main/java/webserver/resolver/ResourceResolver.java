package webserver.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.common.ContentType;

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
        if (fileIn == null) {
            logger.error("File not found: static{}", uri);
            response.send404();
            return;
        }

        byte[] body = fileIn.readAllBytes();
        if (!ContentType.matches(uri)) {
            logger.error("Unsupported content type for URI: {}", uri);
            response.send415();
            return;
        }

        String contentType = ContentType.getContentType(uri);
        response.sendResponse(200, "OK", contentType, body);
    }

}
