package webserver.handler;

import db.Database;
import model.User;
import webserver.request.HttpRequest;
import webserver.request.RequestParser;
import webserver.response.HttpResponse;
import webserver.response.Status;
import webserver.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class PostRequestHandler {
    private final HttpRequest request;

    public PostRequestHandler(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse handle() {
        String path = request.getUrlPath();

        if (path.equals(Constants.CREATION_PATH)) {
            return handleUserCreation();
        } else {
            NotFoundHandler notFoundHandler = new NotFoundHandler(request);
            return notFoundHandler.createNotFoundResponse();
        }
    }

    private HttpResponse handleUserCreation() {
        Map<String, String> bodyParams = new HashMap<>();

        if (request.getHeaders().get(Constants.CONTENT_TYPE.toLowerCase()).equals(Constants.FORM_URL_ENCODED)) {
            bodyParams = RequestParser.parseQueryString(request.getBody());
        }
        User justJoinedUser = new User(bodyParams.get("userId"), bodyParams.get("password"), bodyParams.get("name"), bodyParams.get("email"));
        Database.addUser(justJoinedUser);

        return HttpResponse.getBuilder()
                .httpVersion(Constants.HTTP_VERSION)
                .status(Status.FOUND)
                .header(Constants.LOCATION, Constants.ROOT_PATH)
                .build();
    }
}
