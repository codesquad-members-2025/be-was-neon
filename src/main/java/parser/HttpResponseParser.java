package parser;

import dto.HttpResponse;
import frontHandler.ModelView;
import handler.StaticRequestHandler;
import response.HttpResponseRender;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static session.SessionManager.SESSION_COOKIE_NAME;


public class HttpResponseParser {

    private static final String STATIC_DIRECTORY = "src/main/resources/static";

    public static HttpResponse makeHttpResponse(ModelView mv) throws IOException {

        String viewPath = mv.getViewName();
        Map<String, Object> model = mv.getModel();
        HttpResponse response = new HttpResponse();

        if (viewPath.startsWith("redirect:")) {
            // 리다이렉트 응답
            String redirectPath = viewPath.substring("redirect:".length());
            response.setStatusCode(302);
            response.setStatusText("Found");
            response.getHeaders().put("Location", redirectPath);
            response.setBody(new byte[0]);
            response.setContentType("text/plain");
            // 필요하면 쿠키도 추가
            if (model.containsKey(SESSION_COOKIE_NAME)) {
                response.getCookies().put(SESSION_COOKIE_NAME,model.get(SESSION_COOKIE_NAME).toString());
            }
            return response;
        }

        if (viewPath.endsWith("/")) {
            viewPath += "index.html";
        } else if ("/".equals(viewPath)) {
            viewPath = "/index.html";
        }

        viewPath = URLDecoder.decode(viewPath, StandardCharsets.UTF_8.name());
        File file = new File(STATIC_DIRECTORY + File.separator + viewPath.replace("/", File.separator));

        // 정적 파일 응답
        if (!file.exists() || file.isDirectory()) {
            response = new HttpResponse();
            response.setStatusCode(404);
            response.setStatusText("Not Found");
            response.setContentType("text/html;charset=utf-8");
            response.setBody("<h1>404 Not Found</h1>".getBytes(StandardCharsets.UTF_8));
            return response;
        }


        String contentType = StaticRequestHandler.determineContentType(file);
        response = new HttpResponse();
        response.setStatusCode(200);
        response.setStatusText("OK");
        response.setContentType(contentType);
        // 필요하면 쿠키 등 추가
        if (model.containsKey(SESSION_COOKIE_NAME)) {
            response.getCookies().put(SESSION_COOKIE_NAME,model.get(SESSION_COOKIE_NAME).toString());
        }
        return response;
    }
}
