package webserver.exception;

import webserver.common.HttpStatus;

public class ServerErrorException extends HttpException {
    public ServerErrorException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
