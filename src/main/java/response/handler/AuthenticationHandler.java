package response.handler;

import Exceptions.UnAuthorizedException;
import loader.ResourceData;
import loader.StaticResourceLoader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import session.SessionUserResolver;
import webserver.WebServer;

import static constants.HttpHeaders.CONTENT_LENGTH;
import static constants.HttpHeaders.LOCATION;
import static constants.HttpValues.*;

public class AuthenticationHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);

    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        try {
            User loginUser = SessionUserResolver.getSessionUserFromRequest(request).orElseThrow(
                    () -> new UnAuthorizedException("User not logged in"));

            ResourceData resourceData = StaticResourceLoader.loadResourceData(request.getRequestHeader().getPath());

            byte[] body = resourceData.readAllBytes();

            Response response = Response.builder()
                    .httpVersion(request.getRequestHeader().getHttpVersion())
                    .status(Status.OK)
                    .header(CONTENT_LENGTH, Integer.toString(body.length))
                    .body(body)
                    .build();

            responseSender.send(response);
        } catch (UnAuthorizedException e){
            logger.debug("Unauthorized access attempt: {}", e.getMessage());

            Response response = Response.builder()
                    .httpVersion(request.getRequestHeader().getHttpVersion())
                    .status(Status.FOUND)
                    .header(LOCATION, REDIRECT_LOGIN_PAGE)
                    .header(CONTENT_LENGTH, EMPTY_BODY_LENGTH)
                    .build();

            responseSender.send(response);
        }
    }
}
