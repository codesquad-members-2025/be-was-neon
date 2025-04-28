package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.session.Session;
import webserver.session.SessionManager;
import webserver.util.CookieParser;

import java.io.IOException;

public class AuthHandler implements Handler {
    private final Handler next;

    public AuthHandler(Handler next) {
        this.next = next;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        String cookieHeader = request.getHeaders().get("Cookie");
        String sid = CookieParser.getCookieValue(cookieHeader, "SID");
        Session session = SessionManager.getInstance().findSession(sid);

        // 로그인 정보 없음 → 로그인 페이지로 리다이렉트
        //SID가 있다고 해서 그 사용자가 "로그인한 상태"라고 보장할 수는 없음 -> 반드시 session.getAttribute("user")로 확인해야 함
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login/index.html");
            return;
        }

        // 로그인한 사용자라면 다음 핸들러로 넘기기
        next.handle(request, response);
    }
}
