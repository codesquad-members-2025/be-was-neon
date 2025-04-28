package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.InternalServerErrorException;
import webserver.http.HttpException;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

public class ExceptionHandler implements Handler {
    //checked error를 unchecked error로 만들어주는 게 좋음
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
    private final Handler wrappedHandler;

    public ExceptionHandler(Handler wrappedHandler) {
        this.wrappedHandler = wrappedHandler;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res) throws IOException {
        try{
            //실제 처리 메서드 호출
            wrappedHandler.handle(req, res);
        } catch (HttpException he) {
            he.sendError(res);
        } catch (Exception e){
            log.error("Unhandled exception processing {}", req.getRequestLine(), e);
            new InternalServerErrorException().sendError(res);
        }

    }
}
