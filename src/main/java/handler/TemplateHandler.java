package handler;

import static webserver.common.Constants.EMPTY;

import java.util.function.BiFunction;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

public class TemplateHandler implements Handler{
    private final ResourceLoader resourceLoader;
    private final BiFunction<Session, byte[], byte[]> renderFunction;

    public TemplateHandler(ResourceLoader resourceLoader, BiFunction<Session, byte[], byte[]> renderFunction) {
        this.resourceLoader = resourceLoader;
        this.renderFunction = renderFunction;
    }

    @Override
    public Response handle(Request request) {
        byte[] responseBody = resourceLoader.fileToBytes(request.getRequestUrl(), true);
        Session session = getSessionByCookie(request);
        responseBody = renderFunction.apply(session, responseBody);
        return new Response(HttpStatus.OK, responseBody, EMPTY);
    }
}
