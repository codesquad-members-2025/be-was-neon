package webserver.exception;

import webserver.http.HttpException;

public class MethodNotAllowedException extends HttpException {
    public MethodNotAllowedException() {
        //todo reponse header 잘 들어가는지 확인하기
        super(405, "Method Not Allowed", "<h1>405 Method Not Allowed</h1>");
    }
}
