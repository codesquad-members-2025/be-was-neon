package webserver.request;

import db.Database;
import model.User;
import webserver.response.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class PostRequestHandler {
    private final HttpRequest request;

    private final String POST = "POST";
    private final String ROOT_PATH = "/";
    private final String CREATION_PATH = "/create";
    private final String CONTENT_TYPE = "content-type";
    private final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";

    public PostRequestHandler(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse handle() {
        String path = request.getUrlPath();

        if (path.equals(CREATION_PATH)) {
            return handleUserCreation();
        }
        return HttpResponse.notFound();
    }

    private HttpResponse handleUserCreation() {
        Map<String, String> bodyParams = new HashMap<>();
        if (!request.getMethod().equals(POST)) return HttpResponse.notFound();
        if (request.getHeaders().get(CONTENT_TYPE).equals(FORM_URL_ENCODED)) {
            bodyParams = RequestParser.parseQueryString(request.getBody());
        }
        User justJoinedUser = new User(bodyParams.get("userId"), bodyParams.get("password"), bodyParams.get("name"), bodyParams.get("email"));
        Database.addUser(justJoinedUser);

        return HttpResponse.redirect(ROOT_PATH);
    }
}
