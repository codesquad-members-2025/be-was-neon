package response.handler;

import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import session.SessionManager;

import utils.RequestHeaderValueParser;

import static constants.HttpHeaders.*;
import static constants.HttpValues.EMPTY_BODY_LENGTH;
import static constants.HttpValues.REDIRECT_INDEX_PATH;
import static constants.SpecialChars.EQUALS;
import static constants.SpecialChars.SEMICOLON;

public class LogoutHandler implements Handler {
    private static final String SESSIONID = "SESSIONID";

    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        request.getRequestHeader().getHeaderByKey(COOKIE)
                .map(header -> RequestHeaderValueParser.parseKeyValuePairs(header, SEMICOLON))
                .map(pairs -> RequestHeaderValueParser.getValueByKey(pairs, SESSIONID, EQUALS))
                .ifPresent(SessionManager::invalidateSession);

        Response response = Response.builder()
                .httpVersion(request.getRequestHeader().getHttpVersion())
                .status(Status.FOUND)
                .header(LOCATION, REDIRECT_INDEX_PATH)
                .header(CONTENT_LENGTH, EMPTY_BODY_LENGTH)
                .build();

        responseSender.send(response);
    }
}
