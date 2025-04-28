package webserver.http.response.handler;

import db.Database;
import exception.PasswordMismatchException;
import exception.UserNotFoundException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileContentUtil;
import webserver.http.request.param.BodyParams;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.ResponseBuilder;
import webserver.http.session.Session;
import webserver.http.session.SessionContainer;

import java.util.Optional;

import static webserver.http.common.ContentType.*;
import static webserver.http.common.StatusCode.*;
import static webserver.http.common.UrlPattern.*;

public class DynamicHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(DynamicHandler.class);

    @Override
    public Response handle(Request request) {
        String path = request.getRequestLine("path");
        String method = request.getRequestLine("method");
        BodyParams body = request.getBody();
        Optional<String> sessionId = Optional.empty();

        try {
            if (path.equals(USER_CREATE.getPattern())) {
                createUser(method, body);
            }

            if (path.equals(USER_LOGIN.getPattern())) {
                sessionId = login(method, body);
            }

        } catch (InvalidHttpMethodException e) {
            return handleError(BAD_REQUEST, "error/400.html", e.getMessage());

        } catch (UserNotFoundException | PasswordMismatchException e) {
            return handleError(e.getMessage());
        }

        return new ResponseBuilder(FOUND, "/", sessionId).build();
    }

    private Response handleError(String errorMessage) {
        logger.error("요청 실패: {}",errorMessage);
        Optional<byte[]> errorBody = FileContentUtil.getFileContent(errorPath);
        return new ResponseBuilder(statusCode, errorBody.get(), HTML.getContentType()).build();
    }

    private void createUser(String method, BodyParams body) {
        String userId = body.get("userId");
        String password = body.get("password");
        String name = body.get("name");
        String email = body.get("email");
        User user = new User(userId, password, name, email);
        Database.addUser(user);
    }

    private Optional<String> login(String method, BodyParams body) {
        String loginUserId = body.get("userId");
        String loginUserPw = body.get("password");

        User user = Database.findUserById(loginUserId);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!loginUserPw.equals(user.getPassword())) {
            throw new PasswordMismatchException();
        }

        Session session = new Session();
        session.setAttributes("loginUser", user);

        SessionContainer sessionContainer = SessionContainer.getInstance();
        sessionContainer.add(session);

        return Optional.of(session.getId());
    }
}
