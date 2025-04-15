package handler;

import dto.LoginRequest;
import dto.UserCreateRequest;
import exception.ClientException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.LoginService;
import session.SessionManager;

import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static session.SessionManager.SESSION_COOKIE_NAME;

public class LoginHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    public void handleLoginRequest(String body, OutputStream out){
        try{
            LoginRequest loginRequest = parseQueryString(body);
            User loginUser = LoginService.login(loginRequest);
            String sessionId = SessionManager.createSession(loginUser);
            HttpResponseHelper.sendRedirectWithCookie(out,"/index.html",SESSION_COOKIE_NAME,sessionId,1800,true);
        }catch (ClientException e){
            HttpResponseHelper.sendRedirect(out,"/user/login_failed.html");
        }

    }

    private LoginRequest parseQueryString(String body) {
        logger.info("body info" + body);
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
        return new LoginRequest(
                paramMap.getOrDefault("userId",""),
                paramMap.getOrDefault("password","")
        );
    }
}
