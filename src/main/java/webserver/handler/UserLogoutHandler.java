package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.session.SessionManager;
import webserver.util.CookieParser;

import java.io.IOException;

public class UserLogoutHandler implements Handler{
    private static final Logger log = LoggerFactory.getLogger(UserLogoutHandler.class);
    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        String cookieHeader = request.getHeaders().get("Cookie");
        String sid = CookieParser.getCookieValue(cookieHeader, "SID");

        if (sid != null) {
            SessionManager.getInstance().removeSession(sid);
        }

        //SID 쿠키 만료 시키기 ->  이걸 보내면 브라우저가 내부 쿠키 저장소에서 이 쿠키를 제거함!
        response.addHeader("Set-Cookie", "SID=; Max-Age=0; Path=/; HttpOnly");

        response.sendRedirect("/index.html");
    }
}
