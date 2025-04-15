package exception;

import domain.error.HttpServerError;

public class ServerException extends HttpException{


    public ServerException(String message,int statusCode) {
        super(message,statusCode);
    }

    public ServerException(HttpServerError serverError){
        super(serverError.getDescription(), serverError.getStatusCode());
    }

    public int getStatusCode() {
        return super.getStatusCode();
    }
}
