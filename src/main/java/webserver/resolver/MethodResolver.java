package webserver.resolver;

import handler.article.ArticleDetailHandler;
import handler.auth.CreateUserHandler;
import handler.Handler;
import handler.auth.LoginHandler;
import handler.auth.LogoutHandler;
import handler.view.MainPageHandler;
import handler.util.PathResolvedHandler;
import handler.StaticFileHandler;
import handler.view.TemplateHandler;
import handler.article.WriteArticleHandler;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.HeaderRenderer;
import template.UserListRenderer;
import webserver.common.HttpMethod;
import webserver.exception.MethodNotAllowedException;
import webserver.loader.FileResourceLoader;
import webserver.loader.ResourceLoader;

/**
 * HTTP 요청의 메소드와 경로에 따라 적절한 핸들러를 찾아주는 클래스입니다.
 * 이 클래스는 정적 파일 요청과 동적 요청을 구분하여 처리합니다.
 */
public class MethodResolver {
    private static final Logger logger = LoggerFactory.getLogger(MethodResolver.class);
    private static final ResourceLoader RESOURCE_LOADER = new FileResourceLoader();
    private static final Handler DEFAULT_HANDLER = new StaticFileHandler(RESOURCE_LOADER);
    private static final EnumMap<HttpMethod, Map<String, Handler>> ROUTES = new EnumMap<>(HttpMethod.class);

    static {
        // 초기화
        for (HttpMethod method : HttpMethod.values()) {
            ROUTES.put(method, new HashMap<>());
        }

        // enum 정의 순회하면서 등록
        for (HandlerMapping route : HandlerMapping.values()) {
            if (!route.path.contains("{")) {
                ROUTES.get(route.method).put(route.path, route.handler);
            }
        }
    }

    /**
     * 주어진 HTTP 메소드와 경로에 해당하는 핸들러를 반환합니다.
     * 만약 해당하는 핸들러가 없고, GET 메소드이며 정적 파일이 존재하는 경우
     * StaticFileHandler를 반환합니다.
     *
     * @param path 요청 경로
     * @param method HTTP 메소드
     * @return 해당하는 핸들러
     * @throws MethodNotAllowedException 해당 메소드와 경로에 대한 핸들러가 없는 경우
     */
    public static Handler getHandlerByPath(String path, HttpMethod method) {
        Handler handler = ROUTES.getOrDefault(method, new HashMap<>())
                .get(path);

        if (handler != null) return handler;

        for (HandlerMapping route : HandlerMapping.values()) {
            if (route.method != method || route.pathPattern == null) continue;

            Matcher matcher = route.pathPattern.matcher(path);
            if (matcher.matches()) {
                // matcher.group("id") 등으로 PathVariable 추출도 가능
                Map<String, String> pathVariables = new HashMap<>();
                for (String groupName : getGroupNames(route.pathPattern)) {
                    pathVariables.put(groupName, matcher.group(groupName));
                }
                return new PathResolvedHandler(route.handler, pathVariables);
            }
        }

        // 정적 파일 경로는 존재하는 실제 파일이면 StaticFileHandler로 넘긴다
        if (method == HttpMethod.GET && RESOURCE_LOADER.exists(path)) {
            return DEFAULT_HANDLER;
        }
        throw new MethodNotAllowedException("Method " + method + " not allowed for " + path);
    }

    private static List<String> getGroupNames(Pattern pattern) {
        // 예: "/(?<id>\\d+)"에서 "id" 추출
        Matcher matcher = Pattern.compile("\\(\\?<([^>]+)>").matcher(pattern.pattern());
        List<String> groupNames = new ArrayList<>();
        while (matcher.find()) {
            groupNames.add(matcher.group(1));
        }
        return groupNames;
    }

    /**
     * HTTP 요청과 핸들러를 매핑하는 enum 클래스입니다.
     * 각 경로와 메소드에 대한 핸들러를 정의합니다.
     */
    private enum HandlerMapping {
        DEFAULT(HttpMethod.GET, "", DEFAULT_HANDLER),
        MAIN(HttpMethod.GET, "/", new MainPageHandler(RESOURCE_LOADER)),
        USER_LIST(HttpMethod.GET, "/user/list", new TemplateHandler(RESOURCE_LOADER, List.of(new UserListRenderer()), true)),
        WRITE_ARTICLE_PAGE(HttpMethod.GET, "/article", new TemplateHandler(RESOURCE_LOADER, List.of(new HeaderRenderer()), true, "/article/write.html")),
        ARTICLE_DETAIL(HttpMethod.GET, "/{id}", new ArticleDetailHandler(RESOURCE_LOADER)),
        WRITE_ARTICLE(HttpMethod.POST, "/article", new WriteArticleHandler()),
        CREATE_USER(HttpMethod.POST, "/user/create", new CreateUserHandler()),
        LOGIN_USER(HttpMethod.POST, "/user/login", new LoginHandler()),
        LOGOUT_USER(HttpMethod.POST, "/user/logout", new LogoutHandler());

        HttpMethod method;
        private String path;
        private Handler handler;
        private Pattern pathPattern;

        /**
         * HandlerMapping의 생성자입니다.
         *
         * @param method HTTP 메소드
         * @param path 요청 경로
         * @param handler 해당 경로와 메소드를 처리할 핸들러
         */
        HandlerMapping(HttpMethod method, String path, Handler handler) {
            this.method = method;
            this.path = path;
            this.handler = handler;
            if (path.contains("{")) {
                this.pathPattern = Pattern.compile(pathToRegex(path));
            }
        }
        private String pathToRegex(String path) {
            String regex = "^" + path.replaceAll("\\{([^}]+)}", "(?<$1>\\\\d+)") + "$";
            logger.debug("Path: {} -> Regex : {}", path, regex);
            return regex;
        }
    }
}
