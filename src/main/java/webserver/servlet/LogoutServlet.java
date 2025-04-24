package webserver.servlet;

import util.SessionUtil;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpSession;

import java.io.IOException;

public class LogoutServlet implements HttpServlet {
    private HttpSession httpSession;
    public LogoutServlet(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String sessionId = SessionUtil.getSessionIdFromCookie(request);
        if (sessionId != null) {httpSession.invalidate(sessionId);}

        response.setStatus(302);
        response.addHeader("Location", "/main");
        response.addHeader("Set-Cookie", "SESSIONID=; Max-Age=0; Path=/");
    }
}
