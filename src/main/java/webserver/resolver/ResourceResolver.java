package webserver.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.common.ContentType;
import webserver.http.response.HttpStatusCode;

import java.io.IOException;
import java.io.InputStream;

public class ResourceResolver implements Resolver {

    private static final Logger logger = LoggerFactory.getLogger(ResourceResolver.class);
    private static final String BASE_PATH = "static";
    private final HttpRequest request;
    private final HttpResponse response;

    public ResourceResolver(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void resolve() throws IOException {
        String path = request.getRequestLine().getPath();

        InputStream fileIn = getClass().getClassLoader().getResourceAsStream(BASE_PATH + path);
        if (fileIn == null) {
            logger.error("File not found: static{}", path);
            response.sendResponse(HttpStatusCode.NOT_FOUND, ContentType.HTML, "<h1>File Not Found</h1>".getBytes());
            return;
        }

        byte[] body = fileIn.readAllBytes();
        if (!ContentType.matches(path)) {
            logger.error("Unsupported content type for URI: {}", path);
            response.sendResponse(HttpStatusCode.UNSUPPORTED_MEDIA_TYPE, ContentType.HTML, "<h1>Unsupported Media Type</h1>".getBytes());
            return;
        }

        ContentType contentType = ContentType.getContentType(path);
        response.sendResponse(HttpStatusCode.OK, contentType, body);
    }

}
