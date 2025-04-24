package response.handler;

import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import session.Session;
import session.SessionManager;
import templates.HtmlBuilder;
import templates.TemplateEngine;
import utils.RequestHeaderValueParser;

import java.util.HashMap;
import java.util.Map;

import static constants.HttpHeaders.*;
import static constants.HttpValues.*;
import static constants.SpecialChars.*;

public class DynamicHeaderHandler implements Handler{
    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        //ToDo: 세션에서 사용자 로그인 확인후 모델에 추가하고, response body 생성
        String path = request.getRequestHeader().getPath();

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

        byte[] body = TemplateEngine.render(path, model);

        Response response = Response.builder()
                .httpVersion(request.getRequestHeader().getHttpVersion())
                .status(Status.OK)
                .header(CONTENT_LENGTH, Integer.toString(body.length))
                .body(body)
                .build();

        responseSender.send(response);
    }
}
