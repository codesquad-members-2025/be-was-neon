package handler;

import httpconst.HttpConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;
import response.HttpResponseWriter;
import session.SessionManager;
import utils.RequestParser;

import java.io.IOException;
import java.util.Map;

public class UserLogoutHandler implements Handler{

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseWriter.class);

    @Override
    public void handle(Request request, HttpResponseWriter responseWriter) throws IOException {
        Map<String, String> headers = request.getHeader().getHeaders();

        SessionManager sessionManager = SessionManager.getInstance();
        String sessionId = RequestParser.parseSessionId(headers);
        sessionManager.removeSession(sessionId);
        logger.info(sessionId + " has been logged out");

        responseWriter.sendRedirect(HttpConst.MAIN_PAGE);
    }


}
