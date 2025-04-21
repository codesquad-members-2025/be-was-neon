package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public class DispatcherServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final DispatcherServlet instance = new DispatcherServlet();

    private DispatcherServlet() {
    }

    public static DispatcherServlet getInstance() {
        return instance;
    }

    public void dispatch(HttpRequest request, HttpResponse response) {
        try {
            Controller controller = RequestMapping.getController(request.getPath());
            controller.service(request, response);
        } catch (Exception e) {
            logger.error("요청 처리 중 오류 발생: {}", e.getMessage(), e);
            try {
                response.send500();
            } catch (IOException ex) {
                logger.error("요청 응답 전송 실패", ex);
            }
        }
    }
}
