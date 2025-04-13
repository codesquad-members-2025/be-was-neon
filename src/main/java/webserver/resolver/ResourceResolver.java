package webserver.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.ContentType;
import webserver.http.exception.HttpException;
import webserver.http.request.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import static webserver.http.response.HttpStatusCode.*;

public class ResourceResolver implements Resolver {

    private static final Logger logger = LoggerFactory.getLogger(ResourceResolver.class);
    private static final String BASE_PATH = "static";
    private static final String INDEX_FILE = "index.html";
    private static final String FILE_PROTOCOL = "file";
    private final HttpRequest request;

    public ResourceResolver(HttpRequest request) {
        this.request = request;
    }

    @Override
    public ResolveResponse<byte[]> resolve() {
        String path = request.getRequestLine().getPath();
        URL resourceUrl = resolveResourceUrl(BASE_PATH + path);

        byte[] body = readResourceBytes(resourceUrl);

        if (!ContentType.matches(path)) {
            logger.error("Unsupported content type for URI: {}", path);
            throw new HttpException(UNSUPPORTED_MEDIA_TYPE);
        }

        ContentType contentType = ContentType.getContentType(resourceUrl.toString());
        return ResolveResponse.ok(contentType, body);
    }

    private URL resolveResourceUrl(String fullPath) {
        URL resourceUrl = getClass().getClassLoader().getResource(fullPath);
        if (resourceUrl == null) {
            logger.error("File not found: {}", fullPath);
            throw new HttpException(NOT_FOUND);
        }

        if (resourceUrl.getProtocol().equals(FILE_PROTOCOL)) {
            try {
                File file = new File(resourceUrl.toURI());
                if (file.isDirectory()) {
                    if (!fullPath.endsWith("/")) {
                        fullPath += "/";
                    }
                    fullPath += INDEX_FILE;
                    resourceUrl = getClass().getClassLoader().getResource(fullPath);
                    if (resourceUrl == null) {
                        logger.error("Index file not found in directory: {}", fullPath);
                        throw new HttpException(NOT_FOUND);
                    }
                }
            } catch (URISyntaxException e) {
                logger.error("Invalid URI syntax for resource: {}", e.getMessage());
                throw new HttpException(NOT_FOUND);
            }
        }

        return resourceUrl;
    }

    private byte[] readResourceBytes(URL resourceUrl) {
        try (InputStream in = resourceUrl.openStream()) {
            return in.readAllBytes();
        } catch (IOException e) {
            throw new HttpException(INTERNAL_SERVER_ERROR);
        }
    }

}
