package router;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.ContentType;
import util.FileUtils;
import webserver.common.HttpStatus;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public enum RequestRoute {

    CREATE("/create", RequestRoute::handleUserCreation),;

    private static final Logger logger = LoggerFactory.getLogger(RequestRoute.class);
    private final String path;
    private final RequestHandlerFunctional handler;

    RequestRoute(String path, RequestHandlerFunctional handler) {
        this.path = path;
        this.handler = handler;
    }

    public String getPath() {
        return path;
    }

    public RequestHandlerFunctional getHandler() {
        return handler;
    }

    private static void handleUserCreation(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, List<String>> parameters = request.getParameters();

        if (!containsRequiredParameters(parameters)) {
            response.send(HttpStatus.BAD_REQUEST, ContentType.HTML,
                    "필수 회원 정보가 누락되었습니다".getBytes(StandardCharsets.UTF_8));
        }

        String userId = parameters.get("userId").get(0);
        String password = parameters.get("password").get(0);
        String name = parameters.get("name").get(0);
        String email = parameters.get("email").get(0);

        logger.debug("sign up request - userId: {}, password: {}, name: {}, email: {}",
                userId, password, name, email);

        if (Database.findUserById(userId) != null) {
            logger.info("이미 사용 중인 사용자 ID: {}", userId);
            response.send(HttpStatus.CONFLICT, ContentType.HTML, "이미 사용중인 아이디입니다.".getBytes(StandardCharsets.UTF_8));
            return;
        }

        User user = new User(userId, password, name, email);
        Database.addUser(user);

        response.sendRedirect("/index.html");
    }

    public static RequestRoute findByPath(String path) {
        for (RequestRoute route : values()) {
            if (route.getPath().equals(path)) {
                return route;
            }
        }
        return null;
    }

    private static boolean containsRequiredParameters(Map<String, List<String>> parameters) {
        return parameters.containsKey("userId") &&
                parameters.containsKey("password") &&
                parameters.containsKey("name") &&
                parameters.containsKey("email");
    }

    public static class Router{
        private static final Logger logger = LoggerFactory.getLogger(Router.class);

        private Router() {
        }

        private static final RequestHandlerFunctional staticFileHandler = ((request, response) -> {
            String path = request.getPath();
            ContentType contentType = ContentType.getContentTypeByPath(path);

            try {
                byte[] body = FileUtils.readFileBytes(path);
                response.sendOk(contentType, body);

            } catch (IOException e) {
                logger.error("파일을 찾을 수 없습니다: {}", path);
                response.send404();
            }
        });

        public static void route(HttpRequest request, HttpResponse response) throws IOException {
            String path = request.getPath();
            RequestRoute route = RequestRoute.findByPath(path);

            try {
                if (route != null) {
                    route.getHandler().handle(request, response);
                } else {
                    staticFileHandler.handle(request, response);
                }
            } catch (Exception e) {
                logger.error("요청 처리 중 오류 발생: {}", e.getMessage(), e);
                response.send500();
            }
        }
    }
}
