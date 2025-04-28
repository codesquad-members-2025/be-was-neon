package handler;

import httpconst.HttpConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;
import response.HttpResponseWriter;
import session.SessionManager;

import java.io.IOException;
import java.util.Map;

public class UserLogoutHandler implements Handler{

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseWriter.class);

    @Override
    public void handle(Request request, HttpResponseWriter responseWriter) throws IOException {
        Map<String, String> headers = request.getHeader().getHeaders();
        String cookieHeader = headers.get("Cookie");

        SessionManager sessionManager = SessionManager.getInstance();
        String sessionId;
        if(cookieHeader != null) {
            String[] cookies = cookieHeader.split("; ");
            for (String cookie : cookies) {
                String[] parts = cookie.split("=");
                if (parts.length == 2 && parts[0].equals("JSESSIONID")) {
                    sessionId = parts[1];
                    sessionManager.removeSession(sessionId);
                    break;
                }
            }
        }
        responseWriter.sendRedirect(HttpConst.MAIN_PAGE);
    }


}
