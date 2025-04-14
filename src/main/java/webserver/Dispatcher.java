package webserver;

import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.HTTP_METHOD;
import static webserver.common.Constants.REQUEST_URL;

import db.Database;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.RequestParser;
import webserver.response.Response;

public class Dispatcher {
    private static final String GET = "GET";
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private final ResourceLoader resourceLoader;

    public Dispatcher(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Response dispatchRequest(Map<String, List<String>> requestMap) throws FileNotFoundException {
        byte[] body = new byte[0];
        if (requestMap.get(HTTP_METHOD).getFirst().equals(GET)) {
            if (requestMap.get(REQUEST_URL).getFirst().equals("/user/create")){
                Map<String, String> queryMap = RequestParser.getQueryMap(requestMap);

                User user = new User(queryMap.get("userId"), queryMap.get("name"), queryMap.get("password"),
                        queryMap.get("email"));
                        Database.addUser(user);
                logger.debug("create user : {}", user);
                return new Response(HttpStatus.HTTP_302,  body, "/");
            }
            body = resourceLoader.fileToBytes(requestMap.get(REQUEST_URL).getFirst());
            return new Response(HttpStatus.HTTP_200, body, EMPTY);
        }
        return new Response(HttpStatus.HTTP_404, body, EMPTY);
    }
}
