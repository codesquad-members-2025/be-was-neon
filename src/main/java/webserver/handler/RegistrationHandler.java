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

public class RegistrationHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        String path = request.getUrlPath();
        Map<String, String> bodyParams = new HashMap<>();
        if (request.getHeaders().get(Constants.CONTENT_TYPE.toLowerCase()).equals(Constants.FORM_URL_ENCODED)) {
            bodyParams = RequestParser.parseQueryString(request.getBody());
        }
        User justJoinedUser = new User(bodyParams.get("userId"), bodyParams.get("password"), bodyParams.get("name"), bodyParams.get("email"));
        Database.addUser(justJoinedUser);

        return createResponse();
    }

    private HttpResponse createResponse() {
        return HttpResponse.getBuilder()
                .httpVersion(Constants.HTTP_VERSION)
                .status(Status.FOUND)
                .header(Constants.LOCATION, Constants.ROOT_PATH)
                .build();
    }
}
