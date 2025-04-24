package templates;

import request.Request;
import session.Session;
import session.SessionManager;
import utils.RequestHeaderValueParser;

import java.util.HashMap;
import java.util.Map;

import static constants.HttpHeaders.COOKIE;
import static constants.HttpValues.SESSIONID;
import static constants.SpecialChars.*;

public class HeaderModelBuilder {
    public static Map<String,String> build(Request request){
        Map<String,String> model = new HashMap<>();

        request.getRequestHeader().getHeaderByKey(COOKIE)
                .map(header -> RequestHeaderValueParser.parseKeyValuePairs(header, SEMICOLON))
                .map(pairs -> RequestHeaderValueParser.getValueByKey(pairs, SESSIONID, EQUALS))
                .flatMap(SessionManager::getSession)
                .map(Session::getUser)
                .ifPresentOrElse(user -> {
                            model.put("nickname", HtmlBuilder.headerNickname(user.getNickname()));
                            model.put("loginOrLogout", HtmlBuilder.headerLogoutButton());},
                        () -> {
                            model.put("nickname", EMPTY);
                            model.put("loginOrLogout", HtmlBuilder.headerLoginButton());
                        });
        return model;
    }
}
