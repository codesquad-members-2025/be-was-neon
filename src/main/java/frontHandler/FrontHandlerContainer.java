package frontHandler;

import exception.ClientException;
import handler.HttpResponseHelper;
import handler.LoginHandler;
import handler.StaticRequestHandler;
import handler.UserRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static domain.error.HttpClientError.findByStatusCode;

public class FrontHandlerContainer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FrontHandlerContainer.class);
    private final Socket connection;
    private final StaticRequestHandler staticRequestHandler;
    private final UserRequestHandler userRequestHandler;
    private final LoginHandler loginHandler;

    public FrontHandlerContainer(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.staticRequestHandler = new StaticRequestHandler();
        this.userRequestHandler = new UserRequestHandler();
        this.loginHandler = new LoginHandler();
    }

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP: {}, Port: {}",
                connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {

            // HTTP 요청 파싱
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String requestLine = reader.readLine();
            if (requestLine == null) return;

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                HttpResponseHelper.sendErrorResponse(out, new ClientException(findByStatusCode(400)));
                return;
            }

            String method = requestParts[0];
            String fullPath = requestParts[1];
            logger.debug("Request: {} {}", method, fullPath);

            // 경로와 쿼리스트링 분리
            String[] pathAndQuery = fullPath.split("\\?", 2);
            String path = pathAndQuery[0];
            String queryString = (pathAndQuery.length > 1) ? pathAndQuery[1] : null;

            //헤더 정보
            String line;
            int contentLength = 0;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                if(line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

            //body 정보
            String body = null;
            if(contentLength > 0 && "POST".equalsIgnoreCase(method)) {
                char[] bodyChars = new char[contentLength];
                int read = reader.read(bodyChars,0,contentLength);
                body = new String(bodyChars,0,read);
            }

            // 라우팅 처리
            handleRouting(path, method, queryString,body, out);

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
            HttpResponseHelper.sendRedirect(out, redirectPath);
        }
        // 정적 리소스 처리
        else {
            staticRequestHandler.handleStaticRequest(viewPath, out);
        }
    }

    /*private void handleRouting(String path, String method, String queryString, String body, OutputStream out) throws IOException {
        if ("/create".equals(path) && "POST".equalsIgnoreCase(method)) {
            userRequestHandler.handleCreateUserRequest(body, out);
        } else if ("/update".equals(path) && "GET".equalsIgnoreCase(method)) {
            HttpResponseHelper.sendErrorResponse(out, new ClientException(findByStatusCode(400)));
        } else if("/login".equals(path) && "POST".equalsIgnoreCase(method)) {
            loginHandler.handleLoginRequest(body, out);
        }
        else {
            staticRequestHandler.handleStaticRequest(path, out);
        }
    }*/
}
