package response.handler;

import Exceptions.HttpException;
import db.Database;
import model.User;
import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import session.SessionUserResolver;
import templates.HeaderModelBuilder;
import templates.HtmlBuilder;
import templates.TemplateEngine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static constants.HttpHeaders.*;
import static constants.HttpValues.CONTENT_TYPE_HTML;
import static templates.ModelConstants.USER_LIST;

public class UserListHandler implements Handler {
    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        try {
            String path = request.getRequestHeader().getPath();

            Map<String, String> model = new HashMap<>();

            User loginUser = SessionUserResolver.getSessionUserFromRequest(request).orElseThrow(
                    () -> new HttpException(Status.UNAUTHORIZED, request, "Unauthorized")
            );

            HeaderModelBuilder.build(model, loginUser);

            Collection<User> users = Database.findAll();
            model.put(USER_LIST, HtmlBuilder.userList(users));

            byte[] body = TemplateEngine.render(path, model);

            Response response = Response.builder()
                    .httpVersion(request.getRequestHeader().getHttpVersion())
                    .status(Status.OK)
                    .header(CONTENT_TYPE, CONTENT_TYPE_HTML)
                    .header(CONTENT_LENGTH, Integer.toString(body.length))
                    .body(body)
                    .build();

            responseSender.send(response);
        } catch (HttpException e) {
            Response response = Response.builder()
                    .httpVersion(request.getRequestHeader().getHttpVersion())
                    .status(Status.FOUND)
                    .header(LOCATION, "/")
                    .build();

            responseSender.send(response);
        }
    }
}
