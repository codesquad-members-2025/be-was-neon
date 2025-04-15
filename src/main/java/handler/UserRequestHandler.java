package handler;

import controller.UserController;
import exception.ClientException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

import static domain.error.HttpClientError.BAD_REQUEST;

public class UserRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserRequestHandler.class);

    public void handleCreateUserRequest(String body, OutputStream out) {
        if (body == null || body.isEmpty()) {
            HttpResponseHelper.sendErrorResponse(out, new ClientException(BAD_REQUEST));
            return;
        }

        User createdUser = new UserController().createUser(body);

        // 회원가입 성공 시 index.html로 리다이렉트
        HttpResponseHelper.sendRedirect(out, "/index.html");
    }
}
