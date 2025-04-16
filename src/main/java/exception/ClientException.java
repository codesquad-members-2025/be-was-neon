package exception;

import domain.error.HttpClientError;

public class ClientException extends HttpException {


    public ClientException(HttpClientError httpError) {
        super(httpError.getDescription(), httpError.getStatusCode());
    }


    public ClientException(String message, int statusCode) {
        super(message,statusCode);
    }

    public int getStatusCode() {
        return super.getStatusCode();
    }
}
