package handler;

import controller.UserController;
import exception.ClientException;
import exception.ServerException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static converter.ToJsonConverter.convertUserToJson;

public class UserRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserRequestHandler.class);

    public void handleCreateUserRequest(String queryString, OutputStream out) {
        if (queryString == null || queryString.isEmpty()) {
            throw new ClientException("Missing Parameters", 400);
        }

        try {
            // 사용자 생성 로직
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

        } catch (ClientException e) {
            // 클라이언트 오류는 그대로 전파
            throw e;
        } catch (Exception e) {
            // 서버 오류 처리
            logger.error("User creation failed: {}", e.getMessage());
            throw new ServerException("Internal Server Error", 500);
        }
    }
}
