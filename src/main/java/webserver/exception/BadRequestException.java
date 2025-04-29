package webserver.exception;

import webserver.http.HttpException;

public class BadRequestException extends HttpException {
    public BadRequestException() {
        // 반드시 super(...) 가 첫 문장이어야 함
        super(400, "Bad Request", "<h1>400 Bad Request</h1>");
    }
}
