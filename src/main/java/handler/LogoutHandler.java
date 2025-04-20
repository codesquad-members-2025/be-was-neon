package handler;

import static webserver.common.Constants.BLANK;
import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.EQUAL;
import static webserver.common.Constants.HEADER_COOKIE;
import static webserver.common.Constants.PASSWORD;
import static webserver.common.Constants.SEMI_COLON;
import static webserver.common.Constants.SLASH;
import static webserver.common.Constants.USER_ID;

import db.Database;
import java.util.List;
import java.util.Map;
import model.User;
import webserver.common.HttpStatus;
import webserver.exception.NotRegisteredUserException;
import webserver.request.Request;
import webserver.request.RequestParser;
import webserver.response.Response;
import webserver.session.Session;
import webserver.session.SessionManager;

public class LogoutHandler implements Handler{
    @Override
    public Response handle(Request request) {
        byte[] responseBody = new byte[0];

        Session session = getSessionByCookie(request);
        if (session != null)
            session.invalidate();

        return new Response(HttpStatus.FOUND, responseBody, SLASH);
    }


}
