package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.NotFoundException;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class NotFoundHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(NotFoundHandler.class);
    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        log.warn("잘못된 경로: {}", request.getPath());
        throw new NotFoundException();
    }
}