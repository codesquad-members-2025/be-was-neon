package session;

import model.User;
import request.Request;
import utils.RequestHeaderValueParser;

import java.util.Optional;

import static constants.HttpHeaders.COOKIE;
import static constants.HttpValues.SESSIONID;
import static constants.SpecialChars.*;

public class SessionUserResolver {
    public static Optional<User> getSessionUserFromRequest(Request request) {
        return request.getRequestHeader().getHeaderByKey(COOKIE)
                .map(header -> RequestHeaderValueParser.parseKeyValuePairs(header, SEMICOLON))
                .map(pairs -> RequestHeaderValueParser.getValueByKey(pairs, SESSIONID, EQUALS))
                .flatMap(SessionManager::getSession)
                .map(Session::getUser);
    }
}
