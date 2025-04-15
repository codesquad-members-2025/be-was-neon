package handler;

import controller.UserController;
import domain.error.HttpClientError;
import exception.ClientException;
import exception.HttpException;
import exception.ServerException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static converter.ToJsonConverter.convertUserToJson;
import static domain.error.HttpClientError.*;

public class UserRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserRequestHandler.class);

    public void handleCreateUserRequest(String queryString, OutputStream out) {
        if (queryString == null || queryString.isEmpty()) {
            HttpResponseHelper.sendErrorResponse(out, new ClientException(BAD_REQUEST));
        }

        User createdUser = new UserController().createUser(queryString);
        String jsonResult = convertUserToJson(createdUser);

        // 성공 응답 전송
        HttpResponseHelper.sendResponse(
                out,
                200,
                "OK",
                "application/json",
                jsonResult.getBytes(StandardCharsets.UTF_8)
        );

    }
}
