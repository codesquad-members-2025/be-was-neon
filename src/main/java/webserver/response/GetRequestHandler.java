package webserver.response;

import webserver.loader.FileResourceLoader;
import webserver.loader.FileResult;
import webserver.loader.ResourceLoader;
import webserver.request.HttpRequest;
import webserver.util.ContentType;

import java.io.IOException;

public class GetRequestHandler {
    private final HttpRequest request;

    private final String DEFAULT_MAIN_PAGE = "/index.html";
    private final String ROOT_PATH = "/";

    public GetRequestHandler(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse isStaticRequest() {
        String path = request.getUrlPath();
        if(path.matches(".*\\.(html|css|js|png|jpg|jpeg|gif|ico|svg)$")) {
            return handleStaticResource();
        } else {
            return null;
        }
    }

    public HttpResponse handleStaticResource() {
        String path = request.getUrlPath();

        if (path.equals(ROOT_PATH)) {
            path = DEFAULT_MAIN_PAGE;
        }
        ResourceLoader loader = new FileResourceLoader();
        FileResult fileResult = loader.fileToBytes(path);
//        responseWriter.send200(fileResult.body().length, ContentType.getContentType(fileResult.resolvedPath()), fileResult.body());

        return HttpResponse.ok(ContentType.getContentType(fileResult.resolvedPath()), fileResult.body());
    }
}
