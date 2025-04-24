package webserver.handler;

import webserver.loader.FileResourceLoader;
import webserver.loader.FileResult;
import webserver.loader.ResourceLoader;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.Status;
import webserver.util.Constants;
import webserver.util.ContentType;

public class GetRequestHandler {
    private final HttpRequest request;


    public GetRequestHandler(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse handle() {
        String path = request.getUrlPath();
        if (path.equals(Constants.ROOT_PATH)) path = Constants.DEFAULT_MAIN_PAGE;
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

        return HttpResponse.getBuilder()
                .httpVersion(Constants.HTTP_VERSION)
                .status(Status.OK)
                .header(Constants.CONTENT_TYPE, ContentType.getContentType(fileResult.resolvedPath()))
                .header(Constants.CONTENT_LENGTH, String.valueOf(fileResult.body().length))
                .body(fileResult.body())
                .build();
    }
}
