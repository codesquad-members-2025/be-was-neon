package handler;

import dto.UserCreateRequest;
import exception.ClientException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static domain.error.HttpClientError.BAD_REQUEST;

public class UserRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserRequestHandler.class);

    public void handleCreateUserRequest(String body, OutputStream out) {
        if (body == null || body.isEmpty()) {
            HttpResponseHelper.sendErrorResponse(out, new ClientException(BAD_REQUEST));
            return;
        }

        UserCreateRequest userCreateRequest = parseQueryString(body);

        User createdUser = UserService.createUser(userCreateRequest);

        // 회원가입 성공 시 index.html로 리다이렉트
        HttpResponseHelper.sendRedirect(out, "/index.html");
    }

    private UserCreateRequest parseQueryString(String body) {
        Map<String, String> paramMap = new HashMap<>();
        if (body != null && !body.isEmpty()) {
            Arrays.stream(body.split("&"))
                    .map(param -> param.split("=", 2))
                    .forEach(arr -> {
                        String key = arr[0];
                        String value = arr.length > 1 ? arr[1] : "";
                        paramMap.put(key, URLDecoder.decode(value, StandardCharsets.UTF_8));
                    });
        }
        return new UserCreateRequest(
                paramMap.getOrDefault("userId", ""),
                paramMap.getOrDefault("password", ""),
                paramMap.getOrDefault("name", ""),
                paramMap.getOrDefault("email", "")
        );
    }
}
