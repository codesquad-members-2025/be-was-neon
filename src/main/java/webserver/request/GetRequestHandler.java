package webserver.request;

import webserver.loader.FileResourceLoader;
import webserver.loader.FileResult;
import webserver.loader.ResourceLoader;
import webserver.response.HttpResponse;
import webserver.util.ContentType;

public class GetRequestHandler {
    private final HttpRequest request;

    private final String DEFAULT_MAIN_PAGE = "/index.html";
    private final String ROOT_PATH = "/";

    public GetRequestHandler(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse handle() {
        String path = request.getUrlPath();
        if (path.equals(ROOT_PATH)) path = DEFAULT_MAIN_PAGE;
        return handleStaticResource(path);
//        if (path.matches(".*\\.(html|css|js|png|jpg|jpeg|gif|ico|svg)$")) {
//            return handleStaticResource(path);
//        } else {
//            return null;
//        }
    }

    public HttpResponse handleStaticResource(String path) {
        ResourceLoader loader = new FileResourceLoader();
        FileResult fileResult = loader.fileToBytes(path);

        return HttpResponse.ok(ContentType.getContentType(fileResult.resolvedPath()), fileResult.body());
    }
}
