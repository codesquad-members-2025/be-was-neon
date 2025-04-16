package frontHandler;

import frontHandler.adapter.ReturnViewPathAdapter;
import response.HttpResponseRender;
import handler.LoginHandler;
import handler.StaticRequestHandler;
import handler.UserRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dto.HttpRequest;
import parser.HttpRequestParser;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontHandlerContainer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FrontHandlerContainer.class);
    private final Socket connection;
    private final StaticRequestHandler staticRequestHandler;
    private final UserRequestHandler userRequestHandler;
    private final LoginHandler loginHandler;

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontHandlerContainer(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.staticRequestHandler = new StaticRequestHandler();
        this.userRequestHandler = new UserRequestHandler();
        this.loginHandler = new LoginHandler();
        // 핸들러 매핑 초기화
        initHandlerMappingMap();
        initHandlerAdapters();
    }


    private void initHandlerMappingMap() {
        handlerMappingMap.put("/create", userRequestHandler);
        handlerMappingMap.put("/login", loginHandler);
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ReturnViewPathAdapter());
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP: {}, Port: {}",
                connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {

            // HTTP 요청 파싱
            HttpRequest request = HttpRequestParser.parse(in);

            // 라우팅 처리
            handleRouting(request.path(), request.method(), request.queryString(), request.body(), out);

        } catch (IOException e) {
            logger.info("IOException occur");
        }
    }

    private void handleRouting(String path,
                               String method,
                               String queryString,
                               String body,
                               OutputStream out) throws IOException {

        // 1. 핸들러 매핑 조회
        Object handler = getHandler(path, method);

        // 2. 어댑터 조회
        HandlerAdapter adapter = getHandlerAdapter(handler);

        if (adapter != null) {
            // 3. 어댑터를 통한 핸들러 실행
            ModelView mv = adapter.handle(method, queryString, body, handler);

            // 4. 뷰 렌더링
            renderView(mv, out);
        } else {
            // 기본 정적 리소스 처리
            staticRequestHandler.handleStaticRequest(path, out);
        }
    }

    private Object getHandler(String path, String method) {
        // 경로/메서드 기반 핸들러 매핑 로직 구현
        if ("/create".equals(path) && "POST".equals(method)) {
            return userRequestHandler;
        } else if ("/login".equals(path) && "POST".equals(method)) {
            return loginHandler;
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElse(null);
    }

    private void renderView(ModelView mv, OutputStream out) throws IOException {
        // 모델 데이터를 활용한 뷰 렌더링 로직
        String viewPath = mv.getViewName();
        Map<String, Object> model = mv.getModel();

        // 예시: 리다이렉트 처리
        if (viewPath.startsWith("redirect:")) {
            String redirectPath = viewPath.substring("redirect:".length());
            HttpResponseRender.sendRedirect(out, redirectPath);
        }
        // 정적 리소스 처리
        else {
            staticRequestHandler.handleStaticRequest(viewPath, out);
        }
    }

}
