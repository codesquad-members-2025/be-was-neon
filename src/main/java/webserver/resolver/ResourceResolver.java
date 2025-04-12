package webserver.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.ContentType;
import webserver.http.exception.HttpException;
import webserver.http.request.HttpRequest;

import java.io.IOException;
import java.io.InputStream;

import static webserver.http.response.HttpStatusCode.NOT_FOUND;
import static webserver.http.response.HttpStatusCode.UNSUPPORTED_MEDIA_TYPE;

public class ResourceResolver implements Resolver {

    private static final Logger logger = LoggerFactory.getLogger(ResourceResolver.class);
    private static final String BASE_PATH = "static";
    private final HttpRequest request;

    public ResourceResolver(HttpRequest request) {
        this.request = request;
    }

    @Override
    public ResolveResponse<byte[]> resolve() throws IOException {
        String path = request.getRequestLine().getPath();

        InputStream fileIn = getClass().getClassLoader().getResourceAsStream(BASE_PATH + path);
        if (fileIn == null) {
            logger.error("File not found: static{}", path);
            throw new HttpException(NOT_FOUND);
        }

        byte[] body = fileIn.readAllBytes();
        if (!ContentType.matches(path)) {
            logger.error("Unsupported content type for URI: {}", path);
            throw new HttpException(UNSUPPORTED_MEDIA_TYPE);
        }

        ContentType contentType = ContentType.getContentType(path);
        return ResolveResponse.ok(body, contentType);
    }

}
