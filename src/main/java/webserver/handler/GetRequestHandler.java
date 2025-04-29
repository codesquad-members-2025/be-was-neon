package webserver.handler;

import webserver.loader.FileResourceLoader;
import webserver.loader.FileResult;
import webserver.loader.ResourceLoader;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.Status;
import webserver.util.Constants;
import webserver.util.ContentType;

public class GetRequestHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        String path = request.getUrlPath();
        if (path.equals(Constants.ROOT_PATH)) path = Constants.DEFAULT_MAIN_PAGE;

        ResourceLoader loader = new FileResourceLoader();
        FileResult fileResult = loader.fileToBytes(path);
        return createResponse(fileResult);
    }

    private HttpResponse createResponse(FileResult fileResult) {
        return HttpResponse.getBuilder()
                .httpVersion(Constants.HTTP_VERSION)
                .status(Status.OK)
                .header(Constants.CONTENT_TYPE, ContentType.getContentType(fileResult.resolvedPath()))
                .header(Constants.CONTENT_LENGTH, String.valueOf(fileResult.body().length))
                .body(fileResult.body())
                .build();
    }
}
